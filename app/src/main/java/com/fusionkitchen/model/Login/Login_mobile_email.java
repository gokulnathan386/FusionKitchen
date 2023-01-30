package com.fusionkitchen.model.Login;

import com.google.gson.annotations.SerializedName;

public class Login_mobile_email {

    @SerializedName("message")
    private String message;
    @SerializedName("RESPONSE_CODE")
    private String RESPONSE_CODE;
    @SerializedName("status")
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponsecode() {
        return RESPONSE_CODE;
    }

    public void setResponsecode(String RESPONSE_CODE) {
        this.RESPONSE_CODE = RESPONSE_CODE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
