package com.fusionkitchen.model.menu_model;

import com.google.gson.annotations.SerializedName;

public class ordertype_change_menu_model {

    //AccountResultModel
    @SerializedName("STATUS")
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
