package com.fusionkitchen.model.paymentgatway;

import com.google.gson.annotations.SerializedName;

public class appkey {


    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private appkey.data data;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public appkey.data getData() {
        return data;
    }

    public void setData(appkey.data data) {
        this.data = data;
    }

    public class data{


        @SerializedName("api_id")
        private String api_id;


        @SerializedName("securitykey")
        private String securitykey;


        public String getApi_id() {
            return api_id;
        }

        public void setApi_id(String api_id) {
            this.api_id = api_id;
        }

        public String getSecuritykey() {
            return securitykey;
        }

        public void setSecuritykey(String securitykey) {
            this.securitykey = securitykey;
        }
    }
}
