package com.github.scarecrow.signscognizing.Utilities;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.github.scarecrow.signscognizing.Utilities.SocketCommunicatorThread.MEDIA_TYPE_JSON;

/**
 * Created by Scarecrow on 2018/2/6.
 * 手环对象 数据从服务器拉取
 */

public class Armband {

    public static final int ARMBAND_READY = -1,
            ARMBAND_OCCUPIED = -2;
    /**
     * 0 未匹配
     * 1 作为左手
     * 2 作为右手
     */
    public static int NO_PAIR = 0,
            PAIR_LEFT_HAND = 1,
            PAIR_RIGHT_HAND = 2;
    private String armband_id;
    private int armband_occupy_status;
    private int armband_pair_status = 0;

    public Armband(JSONObject jsonObject) {
        try {
            armband_id = jsonObject.getString("armband_id");
            armband_occupy_status = jsonObject.getInt("armband_status");
        } catch (Exception ee) {
            Log.e(TAG, "Armband constructing : error in parse object json : " + ee);
            ee.printStackTrace();
        }
    }

    public Armband(String raw_json) {
        try {
            JSONObject jsonObject = new JSONObject(raw_json);
            armband_id = jsonObject.getString("armband_id");
            armband_occupy_status = jsonObject.getInt("armband_status");
        } catch (Exception ee) {
            Log.e(TAG, "Armband constructing : error in parse object json : " + ee);
            ee.printStackTrace();
        }
    }

    public void ping() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody
                .create(MEDIA_TYPE_JSON, "?" + "armband_id" + "=" + armband_id);
        // 这里非常奇怪 必须要在第一个参数名前面加上 ? 才能使django接受post内容
        // 然后在进入请求处理方法时 这个问号居然还在参数名上
        Request request = new Request.Builder()
                .url(ArmbandManager.SERVER_IP_ADDRESS + "/ping_armband/")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "ping: ping armband failure");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "ping: ping armband successful");
                }
            }
        });
    }

    public int getArmbandStatusCode() {
        return armband_occupy_status;
    }

    public String getArmbandStatus(){
        String armband_status_str;
        if (armband_occupy_status == ARMBAND_OCCUPIED)
            armband_status_str = "Occupied";
        else
            armband_status_str = "Standby";
        return armband_status_str;
    }

    public String getArmbandId() {
        return armband_id;
    }

    public int getPairStatus() {
        return armband_pair_status;
    }

    public void setPairStatus(int status) {
        armband_pair_status = status;
    }

    @Override
    public String toString() {
        String str, armband_status_str;
        if (armband_occupy_status == ARMBAND_OCCUPIED)
            armband_status_str = "手环已被其他终端使用";
        else
            armband_status_str = "手环就绪，准备接受连接";

        str = "手环ID:\n   " + armband_id
                + "\n手环当前状态:\n   " + armband_status_str;
        return str;
    }
}
