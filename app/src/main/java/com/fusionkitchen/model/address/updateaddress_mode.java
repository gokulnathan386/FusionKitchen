package com.fusionkitchen.model.address;

import com.google.gson.annotations.SerializedName;

public class updateaddress_mode {
    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
