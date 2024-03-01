package com.pigeonchat.Models;

public class DataModel {
    private int img;
    private String msg;
    private String name;

    public DataModel(int img, String msg, String name) {
        this.img = img;
        this.msg = msg;
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

