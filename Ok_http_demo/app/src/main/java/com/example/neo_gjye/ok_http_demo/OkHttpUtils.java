package com.example.neo_gjye.ok_http_demo;

import android.os.Handler;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

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
     * @biref sync post and return String
     *
     * @param url
     * @param params
     * @return String
     */
    public String postSyncString(String url, Param... params) throws IOException
    {
        Response response = postSync(url, params);
        return response.body().string();
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


    /**
     * @biref Async post file
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     */
    public void postAsynFile(String url, Callback callback, File file, String fileKey) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * @biref Async post file
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     */
    public void postAsynFile(String url, Callback callback, File file, String fileKey,Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * @biref Async post file
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @param params
     */
    public void postAsynFile(String url, Callback callback, File[] files, String[] fileKeys, Param... params) throws IOException
    {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }
    private void deliveryResult(Callback callback, Request request)
    {
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params)
    {
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        if(params != null)
            for (Param param : params)
                builder.addFormDataPart(param.getKey(),param.getValue());

        if (files != null)
        {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                Log.d("All","filekey is :   "+ fileKeys[i]+ "fileName is:" + fileName);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
                //builder.addFormDataPart(fileKeys[i],fileName,fileBody); //equal  builder.addPart(Headers.of())
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        Log.d("contentTypeFor","contentTypeFor is :"+contentTypeFor);
        return contentTypeFor;
    }

}
