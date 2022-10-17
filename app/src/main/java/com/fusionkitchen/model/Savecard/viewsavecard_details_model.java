package com.fusionkitchen.model.Savecard;

import com.fusionkitchen.model.wallet.wallet_transaction_model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class viewsavecard_details_model {

    @SerializedName("status")
    private String status;

    @SerializedName("count")
    private String count;

    @SerializedName("message")
    private String message;





    @SerializedName("data")
    private List<viewsavecard_details_model.data> data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<viewsavecard_details_model.data> getData() {
        return data;
    }

    public void setData(List<viewsavecard_details_model.data> data) {
        this.data = data;
    }

    public class data {


        @SerializedName("card_id")
        private String card_id;
        @SerializedName("card_name")
        private String card_name;
        @SerializedName("card_last4")
        private String card_last4;
        @SerializedName("card_exp_month")
        private String card_exp_month;
        @SerializedName("card_exp_year")
        private String card_exp_year;
        @SerializedName("card_image")
        private String card_image;
        @SerializedName("card_holder")
        private String card_holder;


        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getCard_name() {
            return card_name;
        }

        public void setCard_name(String card_name) {
            this.card_name = card_name;
        }

        public String getCard_last4() {
            return card_last4;
        }

        public void setCard_last4(String card_last4) {
            this.card_last4 = card_last4;
        }

        public String getCard_exp_month() {
            return card_exp_month;
        }

        public void setCard_exp_month(String card_exp_month) {
            this.card_exp_month = card_exp_month;
        }

        public String getCard_exp_year() {
            return card_exp_year;
        }

        public void setCard_exp_year(String card_exp_year) {
            this.card_exp_year = card_exp_year;
        }

        public String getCard_image() {
            return card_image;
        }

        public void setCard_image(String card_image) {
            this.card_image = card_image;
        }


        public String getCard_holder() {
            return card_holder;
        }

        public void setCard_holder(String card_holder) {
            this.card_holder = card_holder;
        }
    }
}
