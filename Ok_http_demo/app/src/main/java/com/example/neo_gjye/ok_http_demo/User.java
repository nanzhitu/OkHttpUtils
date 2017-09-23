package com.example.neo_gjye.ok_http_demo;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by neo-gj.ye on 2017/9/21.
 */

public class User {

    public static  final String PARAMJSON = "ParamJson";
    public static  final String PARAMSJSON = "ParamsJson";
    private String username;
    private String password;

    public User(){}

    public User(String username, String password )
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + "]";
    }

    public static Param Users2Param(List<User> userList )
    {
        String UsersJson = JSON.toJSONString(userList);
        Param param ;
        param = new Param();
        param.setKey(PARAMSJSON);
        param.setValue(UsersJson);
        return param;

    }

    public static Param User2Param(User user )
    {
        String userJson = JSON.toJSONString(user);
        Param param = new Param();
        param.setKey(PARAMJSON);
        param.setValue(userJson);
        return param;

    }
}
