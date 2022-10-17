package com.fusionkitchen.model.paymentgatway;

import com.google.gson.annotations.SerializedName;

public class getclientSecret_model {

    //AccountResultModel
    @SerializedName("status")
    private String status;



    @SerializedName("3d_secure")
    private getclientSecret_model.secure secure;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public getclientSecret_model.secure getSecure() {
        return secure;
    }

    public void setSecure(getclientSecret_model.secure secure) {
        this.secure = secure;
    }

    public class secure{

        @SerializedName("d3_payment")
        private String d3_payment;

        @SerializedName("d_secure")
        private String d_secure;



        public String getD3_payment() {
            return d3_payment;
        }

        public void setD3_payment(String d3_payment) {
            this.d3_payment = d3_payment;
        }

        public String getD_secure() {
            return d_secure;
        }

        public void setD_secure(String d_secure) {
            this.d_secure = d_secure;
        }
    }
}
