package com.fusionkitchen.model.cart;

import com.google.gson.annotations.SerializedName;

public class serviceDelCharge_model {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private serviceDelCharge_model.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public serviceDelCharge_model.data getData() {
        return data;
    }

    public void setData(serviceDelCharge_model.data data) {
        this.data = data;
    }

    public class data{
        @SerializedName("service_charge")
        private String service_charge;

        @SerializedName("surcharge")
        private String surcharge;

        @SerializedName("delivery_charge")
        private String delivery_charge;

        @SerializedName("fk_servicecharge")
        private String fk_servicecharge;

        @SerializedName("client_deliverycharge")
        private String client_deliverycharge;

        public String getService_charge() {
            return service_charge;
        }

        public void setService_charge(String service_charge) {
            this.service_charge = service_charge;
        }

        public String getSurcharge() {
            return surcharge;
        }

        public void setSurcharge(String surcharge) {
            this.surcharge = surcharge;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }


        public String getFk_servicecharge() {
            return fk_servicecharge;
        }

        public void setFk_servicecharge(String fk_servicecharge) {
            this.fk_servicecharge = fk_servicecharge;
        }


        public String getclient_deliverycharge() {
            return client_deliverycharge;
        }

        public void setclient_deliverycharge(String client_deliverycharge) {
            this.client_deliverycharge = client_deliverycharge;
        }
    }
}
