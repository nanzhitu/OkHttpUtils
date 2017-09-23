package com.example.neo_gjye.ok_http_demo;

import android.os.Handler;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;

/**
 * Created by neo-gj.ye on 2017/9/19.
 */

public class OkHttpUtils {

    private Handler mDelivery;

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    private static OkHttpUtils mOkHttpUtils = new OkHttpUtils();

    private OkHttpUtils() {}

    public static OkHttpUtils getInstance() {
        return mOkHttpUtils;
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * @biref sync get and return String
     *
     * @param url
     * @return Response
     */
    public String getSyncString(String url) throws IOException {

        String body = null;
        Response response = getSync(url);
        if(response.isSuccessful())
            body =  response.body().string();
        return body;
    }



    /**
     * @biref sync get
     *
     * @param url
     * @return Response
     */
    public Response getSync(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * @biref Async get
     *
     * @param url
     * @param callback
     */
    public void getAsyn(String url, Callback callback)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        deliveryResult(callback, request);
    }

    /**
     * @biref sync post
     *
     * @param url
     * @param params
     * @return Response
     */
    public Response postSync(String url, Param... params) throws IOException
    {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * @biref Async post
     *
     * @param url
     * @param callback
     * @param params
     */
    public  void postAsyn(String url, Callback callback, Param... params)
    {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    private Request buildPostRequest(String url, Param[] params)
    {
        if (params == null)
        {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params)
        {
            builder.add(param.getKey(), param.getValue());
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private void deliveryResult(Callback callback, Request request)
    {
        mOkHttpClient.newCall(request).enqueue(callback);
    }
    
}
