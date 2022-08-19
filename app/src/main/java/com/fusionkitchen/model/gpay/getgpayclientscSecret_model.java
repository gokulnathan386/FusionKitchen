package com.fusionkitchen.model.gpay;

import com.fusionkitchen.model.Savecard.getclientscSecret_model;
import com.fusionkitchen.model.home_model.location_type_modal;
import com.google.gson.annotations.SerializedName;

public class getgpayclientscSecret_model {

    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private getgpayclientscSecret_model.data data;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
