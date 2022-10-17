package com.fusionkitchen.model.paymentmethod;

import com.google.gson.annotations.SerializedName;

import com.fusionkitchen.model.post_code_modal;

public class order_payment_model {


    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private order_payment_model.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public order_payment_model.data getData() {
        return data;
    }

    public void setData(order_payment_model.data data) {
        this.data = data;
    }

    public class data {

        @SerializedName("cash")
        private String cash;
        @SerializedName("card")
        private String card;
        @SerializedName("googlepay")
        private String googlepay;



        public String getCash() {
            return cash;
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getGooglepay() {
            return googlepay;
        }

        public void setGooglepay(String googlepay) {
            this.googlepay = googlepay;
        }
    }

}
