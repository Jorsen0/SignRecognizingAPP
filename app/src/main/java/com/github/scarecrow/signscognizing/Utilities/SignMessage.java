package com.github.scarecrow.signscognizing.Utilities;

import android.util.Log;

import com.github.scarecrow.signscognizing.Utilities.auto_complete.SentenceAutoCompleter;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Scarecrow on 2018/2/6.
 *
 */

public class SignMessage extends ConversationMessage {
    public static final int CONFIRMED_CORRECT = 564,
            CONFIRMED_WRONG = 456,
            NO_RECAPTURE = 789,
            INITIAL = 2541;

    private int sign_feedback_stauts;

    private int capture_id = 0;

    private List<SignSentence> sentences;

    private SignSentence active_sentence;

    //根据当前输入进行补全的结果 active 每添加进一个词时进行更新
    private List<String> complete_result;

    private boolean is_capture_complete = false;


    public SignMessage(String text, int msg_id) {
        super(msg_id, ConversationMessage.SIGN, text);
        sign_feedback_stauts = INITIAL;
        sentences = new LinkedList<>();
        active_sentence = new SignSentence();
        sentences.add(active_sentence);
    }

    public int getSignFeedbackStatus() {
        return sign_feedback_stauts;
    }

    public void setSignFeedbackStatus(int stauts) {
        sign_feedback_stauts = stauts;
    }

    public int getCaptureId() {
        return capture_id;
    }

    public void setCaptureId(int capture_id) {
        this.capture_id = capture_id;
    }

    public boolean isCaptureComplete() {
        return is_capture_complete;
    }

    public void setCaptureComplete(boolean status) {
        is_capture_complete = status;
    }

    protected void appendTextContent(String content) {
        active_sentence.appendWord(content);
        complete_result = SentenceAutoCompleter
                .getInstance()
                .executeValueQuery(active_sentence.getWordSeq(), false);
        Log.d(TAG, "appendTextContent: execute complete" + complete_result);

    }

    public List<String> getCompleteResult() {
        return complete_result;
    }

    /**
     * 用户对一个手语序列输入完毕 点选了补全sentence
     * 将补全结果填回句子，新建下一个active sentence，
     * 开启下一个句子识别过程。
     *
     * @param res 补全结果
     */
    public void setCompleteResult(String res) {
        active_sentence.setSentence(res);
        active_sentence = new SignSentence();
    }


    public void cleanTextContent(){
        sentences.clear();
        active_sentence = new SignSentence();
        sentences.add(active_sentence);
    }

    @Override
    public String getTextContent() {
        StringBuilder ret = new StringBuilder();
        for(SignSentence s:sentences){
            ret.append(s.getSentenceStr());
        }
        return ret.toString();
    }

    public SignSentence getActiveSentence() {
        return active_sentence;
    }


}
