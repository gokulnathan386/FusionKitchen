package com.fusionkitchen.model.menu_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class collDelivery_model {


    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private collDelivery_model.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public collDelivery_model.data getData() {
        return data;
    }

    public void setData(collDelivery_model.data data) {
        this.data = data;
    }

    public class data{

        @SerializedName("delivery")
        private String delivery;


        @SerializedName("collection")
        private String collection;

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }
    }


}
