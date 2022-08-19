package com.fusionkitchen.model.menu_model;

import com.google.gson.annotations.SerializedName;

public class final_addon_add_model {

    @SerializedName("status")
    private String status;

    @SerializedName("error_code")
    private String error_code;


    @SerializedName("error_message")
    private String error_message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
