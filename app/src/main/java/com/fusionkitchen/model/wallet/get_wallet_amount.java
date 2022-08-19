package com.fusionkitchen.model.wallet;

import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.google.gson.annotations.SerializedName;

public class get_wallet_amount {



    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private get_wallet_amount.amountdata data;



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

        @SerializedName("id")
        private String id;

        @SerializedName("reg_date")
        private String reg_date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getReg_date() {
            return reg_date;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }
    }
}
