package com.fusionkitchen.model.gpay;

import com.google.gson.annotations.SerializedName;

public class getgpaystuartpayment_model {


    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("stripe_amount")
    private String stripe_amount;

    @SerializedName("data")
    private getgpayclientscSecret_model.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStripeamount() {
        return stripe_amount;
    }

    public void setStripeamount(String stripe_amount) {
        this.stripe_amount = stripe_amount;
    }

    public getgpayclientscSecret_model.data getData() {
        return data;
    }

    public void setData(getgpayclientscSecret_model.data data) {
        this.data = data;
    }

    public class data{

        @SerializedName("client_secret")
        private String client_secret;

        public String getClient_secret() {
            return client_secret;
        }

        public void setClient_secret(String client_secret) {
            this.client_secret = client_secret;
        }
    }



}
