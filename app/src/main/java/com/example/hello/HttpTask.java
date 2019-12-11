package com.example.hello;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpTask implements Runnable {
    String path ;
    String data;
    Handler handler;
    RequestBody body;

    public HttpTask(String path, String data, Handler handler,RequestBody body) {
        this.path = path;
        this.data = data;
        this.handler = handler;
        this.body=body;
    }
    OkHttpClient client = new OkHttpClient();
    @Override
    public void run() {
        Log.i(null,"开始请求path："+path);
        Log.i(null,"开始请求data："+data);
        //Toast.makeText(MainActivity.this,"保存数据："+msg,(short)0).show();


       // RequestBody body = RequestBody.create(null,data);
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            String res = response.body().string();
            Log.i(null,"请求结果："+res);
            Message message = handler.obtainMessage();
            message.what=1;
            message.obj=res;
            handler.sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
