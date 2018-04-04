package com.github.scarecrow.signscognizing.Utilities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.github.scarecrow.signscognizing.activities.MainActivity.APP_CONTEXT;

/**
 * Created by Scarecrow on 2018/2/8.
 * 消息id分为两种 一种是本地的id 给予文字和语音识别消息的id
 * 这些消息不需要和服务器进行交互 仅在本地进行管理即可。
 * 还有一种是手语消息的id 由于手语可能需要进行重发
 * 所以需要与服务端进行进行同步工作 手语数据主要是在服务端进行管理
 * 为了便于服务端手语数据的管理 这个id从服务端获取。
 */

public class MessageManager {
    private static MessageManager instance = new MessageManager();
    private SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
    private SynthesizerListener synthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.d(TAG, "onSpeakBegin: ");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            Log.d(TAG, "onCompleted: ");
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };
    private List<ConversationMessage> messages_list;
    private List<NoticeMessageChanged> notice_list = new ArrayList<>();
    private Map<Integer, SignMessage> sign_message_map = new Hashtable<>();
    private boolean capture_state = false;
    /**
     * 这个方法被用于inputControl的fragment中
     * 在这个fragment中点按创建的手语识别消息都是新建的
     * 该方法先新建一个手语消息实例并并显示出来 并将该实例暂存下来
     * 当时识别完成后使用回调更新该手语的数据
     */
    private SignMessage new_added_msg;
    // false -> 没有采集
    // true  -> 采集中


    private MessageManager() {
        messages_list = new ArrayList<>();


    }

    public static MessageManager getInstance() {
        return instance;
    }

    private int acquire_curr_id() {
        return messages_list.size();
    }

    public void buildSignMessage() {
        new_added_msg = new SignMessage("正在识别手语中", 0);
        messages_list.add(new_added_msg);
        noticeAllTargetMsgAdded();
    }

    public TextMessage buildTextMessage(String text) {
        TextMessage new_msg = new TextMessage(acquire_curr_id(), text);
        messages_list.add(new_msg);
        noticeAllTargetMsgAdded();
        return new_msg;
    }

    public VoiceMessage buildVoiceMessage(String voice_path) {
        VoiceMessage new_msg = new VoiceMessage(acquire_curr_id(), voice_path);
        messages_list.add(new_msg);
        noticeAllTargetMsgAdded();
        return new_msg;
    }

    public void processSignMessageFeedback(String feedback_json) {
        try {
            JSONObject jsonObject = new JSONObject(feedback_json);
            String control_info = jsonObject.getString("control");
            if (control_info.equals("update_recognize_res")) {
                processSignMessageFeedback(jsonObject.getString("text"),
                        jsonObject.getInt("sign_id"),
                        jsonObject.getInt("capture_id"));

            } else if (control_info.equals("end_recognize")) {
                int sign_id = jsonObject.getInt("sign_id");
                if (sign_message_map.containsKey(sign_id)) {
                    sign_message_map.get(sign_id).setCaptureComplete(true);
                    noticeAllTargetSignCaptureEnd();
                    noticeAllTargetMsgChange();
                }
            }
        } catch (Exception ee) {
            Log.e(TAG, "buildSignMessage:  error: " + ee);
            ee.printStackTrace();
        }
    }

    /**
     * 当返回一条手语识别的消息调用后 更新一个手语消息的实例
     * 有两种情况 一种是新创建的手语消息实例 另一种是重发的手语
     * 通过手语的id进行map判断这个手语是否被识别过一次
     *
     * @param text          手语的文字内容 来自服务器
     * @param sign_id       手语的id码 来自服务器给定
     * @return 新生成的手语消息对象
     */
    private void processSignMessageFeedback(String text, int sign_id, int capture_id) {
        SignMessage new_msg;
        if (sign_message_map.containsKey(sign_id)) {
            new_msg = sign_message_map.get(sign_id);
            synthesizeVoice(text, new_msg.getTextContent());
            new_msg.setTextContent(text);
        } else {
            synthesizeVoice(text, "");
            new_added_msg.setCaptureId(capture_id);
            new_added_msg.setTextContent(text);
            new_added_msg.setMsgId(sign_id);
            sign_message_map.put(sign_id, new_added_msg);
        }
        noticeAllTargetMsgChange();
    }

    private void synthesizeVoice(String new_text, String history_text) {
        String voice_str = new_text.substring(history_text.length());
        synthesizer.startSpeaking(voice_str, synthesizerListener);
    }


    public boolean requestCaptureSign() {
        if (capture_state) {
            Log.e(TAG, "requestCaptureSign: sign capturing repeat");
            return false;
        }
        capture_state = true;
        try {
            SocketConnectionManager.getInstance()
                    .sendMessage(buildSignRecognizeRequest(0));
            MessageManager.getInstance()
                    .buildSignMessage();
        } catch (Exception ee) {
            Log.e(TAG, "requestCaptureSign: can not create request, " + ee.getMessage());
            Toast.makeText(APP_CONTEXT,
                    "与服务器连接已断开，请退出后重新连接手环再发起识别请求",
                    Toast.LENGTH_LONG)
                    .show();
            capture_state = false;
            return false;
        }
        return true;
    }

    public boolean recaptureSignRequest(SignMessage message) {
        if (capture_state) {
            Log.e(TAG, "requestCaptureSign: sign capturing repeat");
            return false;
        }
        try {
            capture_state = true;
            message.setCaptureComplete(false);
            noticeAllTargetMsgSignCaptureStart();
            SocketConnectionManager.getInstance()
                    .sendMessage(buildSignRecognizeRequest(message.getMsgId()));
        } catch (Exception ee) {
            Log.e(TAG, "requestCaptureSign: can not create request, " + ee.getMessage());
            capture_state = false;
            Toast.makeText(APP_CONTEXT,
                    "与服务器连接已断开，请退出后重新连接手环再发起识别请求",
                    Toast.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    public boolean stopSignRecognize() {
        if (capture_state) {
            SocketConnectionManager.getInstance()
                    .sendMessage(buildStopCaptureRequest());
            capture_state = false;
            noticeAllTargetSignCaptureEnd();
            return true;
        } else {
            Log.e(TAG, "requestCaptureSign: sign capturing didn't start");
            return false;
        }

    }

    /**
     * 手语识别请求体构造
     * 如果是新增识别， 的 sign_id字段使用0 标识
     * 如："data": {"sign_id" :0}
     *
     * @return 请求的json
     */
    private String buildSignRecognizeRequest(int sign_id) throws Exception {
        Armband[] paired_armbands = ArmbandManager.getArmbandsManger()
                .getCurrentConnectedArmband();
        JSONArray armbands_json_array = new JSONArray();
        for (Armband armband : paired_armbands) {
            if (armband == null)
                throw new Exception("paired armband lose");
            armbands_json_array.put(armband.getArmbandId());
        }
        JSONObject request_body = new JSONObject();
        try {
            request_body.accumulate("control", "sign_cognize_request");
            JSONObject data = new JSONObject();
            data.accumulate("armband_id", armbands_json_array);
            data.accumulate("sign_id", sign_id);
            request_body.accumulate("data", data);
        } catch (Exception ee) {
            Log.e(TAG, "buildSignRecognizeRequest: on build request json " + ee);
            ee.printStackTrace();
        }
        return request_body.toString();
    }


    private String buildStopCaptureRequest() {
        JSONObject request_body = new JSONObject();
        try {
            request_body.accumulate("control", "stop_recognize");
            request_body.accumulate("data", "");
        } catch (Exception ee) {
            Log.e(TAG, "buildStopCaptureRequest: ", ee);
            ee.printStackTrace();
        }
        return request_body.toString();
    }


    public boolean isCapturingSign() {
        return capture_state;
    }

    public List<ConversationMessage> getMessagesList() {
        return messages_list;
    }

    public void cleanMessageList() {
        messages_list.clear();
        noticeAllTargetMsgChange();
    }

    public void addNewNoticeTarget(NoticeMessageChanged obj) {
        notice_list.add(obj);
    }

    private void noticeAllTargetSignCaptureEnd() {
        capture_state = false;
        for (NoticeMessageChanged obj : notice_list) {
            obj.onSignCaptureEnd();
        }
    }

    private void noticeAllTargetMsgAdded() {
        for (NoticeMessageChanged obj : notice_list) {
            obj.onNewMessageAdd();
        }
    }

    public void noticeAllTargetMsgChange() {
        for (NoticeMessageChanged obj : notice_list) {
            obj.onMessageContentChange();
        }
    }

    private void noticeAllTargetMsgSignCaptureStart() {
        for (NoticeMessageChanged obj : notice_list) {
            obj.onSignCaptureStart();
        }
    }

    public interface NoticeMessageChanged {
        void onNewMessageAdd();
        void onMessageContentChange();
        void onSignCaptureStart();
        void onSignCaptureEnd();
    }


    //dubug
/*
    public void sampleDisplayCreate(){
        buildSignMessage();
        processSignMessageFeedback("医生 您好",2,1);
        processSignMessageFeedback("{\"control\":\"end_recognize\",\"sign_id\":2}");
        noticeAllTargetSignCaptureEnd();

        VoiceMessage v = buildVoiceMessage("");
        v.setTextContent("您好，请问您那里不舒服？");
        noticeAllTargetMsgChange();

        buildSignMessage();
        processSignMessageFeedback("我 发烧 了，嗓子 很痛",3,2);
        processSignMessageFeedback("{\"control\":\"end_recognize\",\"sign_id\":3}");
        noticeAllTargetSignCaptureEnd();

        v = buildVoiceMessage("");
        v.setTextContent("来，你张开嘴，我看下你的喉咙。");
        noticeAllTargetMsgChange();

        v = buildVoiceMessage("");
        v.setTextContent("看起来是嗓子发炎了。你发烧多久了，体温多少度？");
        noticeAllTargetMsgChange();

        buildSignMessage();
        processSignMessageFeedback("2 天 ，38 点 8 ",4,3);
        processSignMessageFeedback("{\"control\":\"end_recognize\",\"sign_id\":4}");
        noticeAllTargetSignCaptureEnd();

        v = buildVoiceMessage("");
        v.setTextContent("这几天有受凉吗，身边人有没有生病感冒？");
        noticeAllTargetMsgChange();

        buildSignMessage();
        processSignMessageFeedback("发烧 前 1 天 晚上 跑步 ，母亲 生病 了",5,4);
        processSignMessageFeedback("{\"control\":\"end_recognize\",\"sign_id\":5}");
        noticeAllTargetSignCaptureEnd();


        v = buildVoiceMessage("");
        v.setTextContent("好的，我给你开张单子，你先去二楼验个血，等一会就能出结果。" +
                            "然后你再回来找我，我看情况给你开药或者是打针");
        noticeAllTargetMsgChange();

        buildSignMessage();
        processSignMessageFeedback("好的 谢谢 医生",6,5);
        processSignMessageFeedback("{\"control\":\"end_recognize\",\"sign_id\":6}");
        noticeAllTargetSignCaptureEnd();
    }*/
}
