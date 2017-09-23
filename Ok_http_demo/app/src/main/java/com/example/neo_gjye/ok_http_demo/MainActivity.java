package com.example.neo_gjye.ok_http_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import static com.example.neo_gjye.ok_http_demo.OkHttpUtils.getInstance;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button get_sync;
    Button get_asyn;
    Button post_asyn;
    Response response;
    public static final String URL = "http://192.168.183.1:8080/Okhttp_server_demo/LoginServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.text);
        get_asyn = (Button)findViewById(R.id.button2);
        get_sync = (Button)findViewById(R.id.button);
        post_asyn = (Button)findViewById(R.id.button3);

        get_asyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInstance().getAsyn(URL, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        final String json = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(json);
                            }
                        });
                    }
                });
            }
        });

        get_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = null;
                        try {
                            response = getInstance().getSyncString(URL);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("result","result:"+ response);

                    }
                }).start();

            }
        });

        post_asyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("123","456");
                getInstance().postAsyn(URL, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d("error","error:");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        User user = JSON.parseObject(response.body().string(),User.class);
                        Log.d("result","result:"+user);
                    }
                },User.User2Param(user));
            }
        });
    }
}
