package com.fusionkitchen.model.favorite;

import com.google.gson.annotations.SerializedName;

public class insertfavorite_mode {


    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private String data;

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
