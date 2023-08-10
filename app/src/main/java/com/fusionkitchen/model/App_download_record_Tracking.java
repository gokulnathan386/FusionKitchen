package com.fusionkitchen.model;

import com.google.gson.annotations.SerializedName;

public class App_download_record_Tracking {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("RESPONSE_CODE")
    private String RESPONSE_CODE;

    public String getSTATUS() {
        return status;
    }

    public void setSTATUS(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    public void setRESPONSE_CODE(String RESPONSE_CODE) {
        this.RESPONSE_CODE = RESPONSE_CODE;
    }

}