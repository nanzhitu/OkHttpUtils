package com.example.neo_gjye.ok_http_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.IOException;
import static com.example.neo_gjye.ok_http_demo.OkHttpUtils.getInstance;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button get_sync;
    Button get_asyn;
    Button post_asyn;
    Button post_sync;
    Button post_Asyn_file;
    Button getPost_Asyn_file_data;
    public static final String IP = "http://192.168.1.101:8080";
    public static final String PROJECT = "OkhttpServerDemo";
    public static final String URL_LOGIN = IP + "/"+PROJECT+"/"+"LoginServlet";
    public static final String URL_FILE = IP + "/"+PROJECT+"/"+"FileServlet";
    public static final String FILE = "/qqq.PNG";
    public static final String FILEKEY = "file";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.text);
        get_asyn = (Button)findViewById(R.id.button2);
        get_sync = (Button)findViewById(R.id.button);
        post_asyn = (Button)findViewById(R.id.button3);
        post_sync = (Button)findViewById(R.id.button4);
        post_Asyn_file = (Button)findViewById(R.id.button5);
        getPost_Asyn_file_data = (Button)findViewById(R.id.button6);
        get_asyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInstance().getAsyn(URL_LOGIN, new Callback() {
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
                            response = getInstance().getSyncString(URL_LOGIN);
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
                getInstance().postAsyn(URL_LOGIN, new Callback() {
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

        post_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User("123","456");
                        String response = null;
                        try {
                            response = getInstance().postSyncString(URL_LOGIN,User.User2Param(user));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("post sync result:","is:"+response );
                    }
                }).start();


            }
        });


        post_Asyn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(getFilesDir().getPath()+FILE);
                try {
                    getInstance().postAsynFile(URL_FILE, new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d("error","error:"+request.body().toString());
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            Log.d("post Async file result:","is:"+response );
                        }
                    },
                            file,
                            FILEKEY
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        getPost_Asyn_file_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getFilesDir().getPath()+FILE);
                User user = new User("123","456");
                try {
                    getInstance().postAsynFile(URL_FILE, new Callback() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    Log.d("error","error:"+request.body().toString());
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    Log.d("post Async file result:","is:"+response );
                                }
                            },
                            file,
                            FILEKEY,
                            User.User2Param(user)
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
