package com.example.neo_gjye.ok_http_demo;

/**
 * Created by neo-gj.ye on 2017/9/21.
 */

public class Param {

    private String key;
    private String value;

    public Param()
    {
    }

    public Param(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }


}
