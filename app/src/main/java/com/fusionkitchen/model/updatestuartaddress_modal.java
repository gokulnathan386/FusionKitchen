package com.fusionkitchen.model;

import com.google.gson.annotations.SerializedName;

public class updatestuartaddress_modal{

    @SerializedName("data")
    private String data;

    @SerializedName("status")
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



}
