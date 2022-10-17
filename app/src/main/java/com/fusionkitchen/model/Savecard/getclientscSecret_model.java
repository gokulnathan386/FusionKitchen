package com.fusionkitchen.model.Savecard;

import com.fusionkitchen.model.paymentgatway.getclientSecret_model;
import com.google.gson.annotations.SerializedName;

public class getclientscSecret_model {


    //AccountResultModel
    @SerializedName("status")
    private String status;



    @SerializedName("data")
    private getclientscSecret_model.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public getclientscSecret_model.data getData() {
        return data;
    }

    public void setData(getclientscSecret_model.data data) {
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
