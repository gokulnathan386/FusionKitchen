package com.fusionkitchen.model.wallet;

import com.fusionkitchen.model.address.getaddAddress_mode;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class wallet_transaction_model {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;


    @SerializedName("data")
    private List<wallet_transaction_model.tranferdetails> data;


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

    public List<tranferdetails> getData() {
        return data;
    }

    public void setData(List<tranferdetails> data) {
        this.data = data;
    }

    public class tranferdetails {


        @SerializedName("transfer_amount")
        private String transfer_amount;

        @SerializedName("transfer_date")
        private String transfer_date;


        @SerializedName("transfer_image")
        private String transfer_image;


        @SerializedName("transfer_word")
        private String transfer_word;

        @SerializedName("transfer_symbol")
        private String transfer_symbol;


        public String getTransfer_amount() {
            return transfer_amount;
        }

        public void setTransfer_amount(String transfer_amount) {
            this.transfer_amount = transfer_amount;
        }

        public String getTransfer_date() {
            return transfer_date;
        }

        public void setTransfer_date(String transfer_date) {
            this.transfer_date = transfer_date;
        }

        public String getTransfer_image() {
            return transfer_image;
        }

        public void setTransfer_image(String transfer_image) {
            this.transfer_image = transfer_image;
        }

        public String getTransfer_symbol() {
            return transfer_symbol;
        }

        public void setTransfer_symbol(String transfer_symbol) {
            this.transfer_symbol = transfer_symbol;
        }

        public String getTransfer_word() {
            return transfer_word;
        }

        public void setTransfer_word(String transfer_word) {
            this.transfer_word = transfer_word;
        }
    }
}
