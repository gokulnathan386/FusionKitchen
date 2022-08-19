package com.fusionkitchen.model.wallet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class wallet_walletbutton_model {


    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;


    @SerializedName("data")
    private wallet_walletbutton_model.amountdata data;





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

    public amountdata getData() {
        return data;
    }

    public void setData(amountdata data) {
        this.data = data;
    }

    public class amountdata {


        @SerializedName("amount")
        private String amount;


        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
