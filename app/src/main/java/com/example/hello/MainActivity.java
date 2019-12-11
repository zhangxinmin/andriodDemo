package com.example.hello;

import android.app.DownloadManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
  private EditText id;
  private EditText name;
  private EditText area;
  private EditText rate;
  private Button loadBtn;
  private Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        id=findViewById(R.id.id);
        name=findViewById(R.id.name);
        area=findViewById(R.id.area);
        rate=findViewById(R.id.rate);

        loadBtn=findViewById(R.id.load);
        saveBtn=findViewById(R.id.save);
        loadBtn.setOnClickListener(new LoadListener());
        saveBtn.setOnClickListener(new Saveistener());
    }



    private class LoadListener implements View.OnClickListener {
        private final OkHttpClient client = new OkHttpClient();
        private String obj2Str(Object obj){
            if(null==obj)return "";
            return obj.toString();
        }

      /*  Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                String idText = id.getText().toString().trim();
                String path ="http://10.238.98.201:8080/api/get?id="+idText;
                //HttpClient httpClient = new DefaultHttpClient();
                //Toast.makeText(MainActivity.this,"根据唯一码【"+idText+"】加载数据",(short)0).show();
                Request request = new Request.Builder()
                        .url(path)
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
                    JSONObject jso = new JSONObject(res);
                    String succ=jso.getString("success");

                    if("true".equals(succ)){
                        JSONObject data =jso.getJSONObject("data");
                        String namestr =obj2Str(data.getString("name"));
                        String codestr =obj2Str(data.getString("code"));
                        String posstr =obj2Str(data.getString("pos"));
                        String ratestr =obj2Str(data.getString("rate"));
                        name.setText(namestr);
                        area.setText(posstr);
                        rate.setText(ratestr);
                    }else{
                        String msg=jso.getString("message");
                        Toast.makeText(MainActivity.this,"根据唯一码【"+idText+"】加载数据失败,msg:"+msg,(short)0).show();
                    }
                } catch (Exception e) {
                    Log.i("error","异常："+e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"根据唯一码【"+idText+"】加载数据发生异常,msg:"+e.getMessage(),(short)0).show();
                }
            }
        };*/

        @Override
        public void onClick(View v) {
            String idText = id.getText().toString().trim();
            String path ="http://10.238.98.201:8080/api/get";

            if(TextUtils.isEmpty(idText)){
                Toast.makeText(MainActivity.this,"请先输入唯一码",(short)0).show();
            }else{

                RequestBody body = new FormBody.Builder()
                        .add("id",idText)
                       .build();
                //new Thread(networkTask).start();
                //Toast.makeText(MainActivity.this,"加载",(short)0).show();
                new Thread(new HttpTask(path,"id="+idText,new Handler(){

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        try{
                            String info= (String) msg.obj;
                            JSONObject jso = new JSONObject(info);
                            String succ=jso.getString("success");
                            if("true".equals(succ)){
                                JSONObject data =jso.getJSONObject("data");
                                String namestr =obj2Str(data.getString("NAME"));
                                //String codestr =obj2Str(data.getString("CODE"));
                                String posstr =obj2Str(data.getString("POS"));
                                String ratestr =obj2Str(data.getString("RATE"));
                                name.setText(namestr);
                                area.setText(posstr);
                                rate.setText(ratestr);
                                Toast.makeText(MainActivity.this,"查询完成",(short)0).show();
                            }else{
                                String mm=jso.getString("msg");
                                Toast.makeText(MainActivity.this,"加载数据失败,msg:"+mm,(short)0).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();;
                            Log.e(null,"数据处理异常");
                            Toast.makeText(MainActivity.this,"加载数据异常,msg:"+e.getMessage(),(short)0).show();
                        }

                    }

                },body)).start();
            }
        }
    }

    private class Saveistener implements View.OnClickListener{
        private final OkHttpClient client = new OkHttpClient();

    /*    Runnable networkTask = new Runnable() {
            @Override
            public void run() {
                String idStr=id.getText().toString().trim();
                String nameStr=name.getText().toString().trim();
                String areaStr=area.getText().toString().trim();
                String rateStr=rate.getText().toString().trim();
                String path ="http://10.238.98.201:8080/api/save";
                //Toast.makeText(MainActivity.this,"保存数据："+msg,(short)0).show();
                RequestBody body = RequestBody.create(null,"&id="+idStr+"&name="+nameStr+"&code=&rate="+rateStr+"&area="+areaStr);
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
                    JSONObject jso = new JSONObject(res);
                    String succ = jso.getString("success");

                    if("true".equals(succ)){
                        Toast.makeText(MainActivity.this,"保存成功",(short)0).show();
                    }else{
                        String info=jso.getString("message");
                        Toast.makeText(MainActivity.this,"保存失败,msg:"+info,(short)0).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"保存发生异常,msg:"+e.getMessage(),(short)0).show();
                }
            }
        };*/
        @Override
        public void onClick(View v) {
            //new Thread(networkTask).start();
            //Toast.makeText(MainActivity.this,"保存",(short)0).show();
            String path ="http://10.238.98.201:8080/api/save";
            String idStr=id.getText().toString().trim();
            String nameStr=name.getText().toString().trim();
            String areaStr=area.getText().toString().trim();
            String rateStr=rate.getText().toString().trim();
            RequestBody body = new FormBody.Builder()
                    .add("id",idStr)
                    .add("name",nameStr)
                    .add("pos",areaStr)
                    .add("rate",rateStr)
                    .build();
            //new Thread(networkTask).start();
            //Toast.makeText(MainActivity.this,"加载",(short)0).show();
            new Thread(new HttpTask(path,"id=",new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    try{
                        String info= (String) msg.obj;
                        JSONObject jso = new JSONObject(info);
                        String succ=jso.getString("success");
                        if("true".equals(succ)){
                            Toast.makeText(MainActivity.this,"保存完成",(short)0).show();
                        }else{
                            String mm=jso.getString("msg");
                            Toast.makeText(MainActivity.this,"保存数据失败,msg:"+mm,(short)0).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();;
                        Log.e(null,"数据处理异常");
                        Toast.makeText(MainActivity.this,"保存数据异常,msg:"+e.getMessage(),(short)0).show();
                    }

                }

            },body)).start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
