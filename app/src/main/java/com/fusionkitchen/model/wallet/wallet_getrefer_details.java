package com.fusionkitchen.model.wallet;

import com.fusionkitchen.model.paymentgatway.completpay_model;
import com.google.gson.annotations.SerializedName;

public class wallet_getrefer_details {

    //AccountResultModel
    @SerializedName("status")
    private String status;


    @SerializedName("msg")
    private String msg;

    @SerializedName("refno")
    private wallet_getrefer_details.refno refno;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public wallet_getrefer_details.refno getRefno() {
        return refno;
    }

    public void setRefno(wallet_getrefer_details.refno refno) {
        this.refno = refno;
    }

    public class refno {


        @SerializedName("transfer")
        private String transfer;
        @SerializedName("transfer_group")
        private String transfer_group;
        @SerializedName("3d_secure")
        private String d_secure;


        public String getTransfer() {
            return transfer;
        }

        public void setTransfer(String transfer) {
            this.transfer = transfer;
        }

        public String getTransfer_group() {
            return transfer_group;
        }

        public void setTransfer_group(String transfer_group) {
            this.transfer_group = transfer_group;
        }

        public String getD_secure() {
            return d_secure;
        }

        public void setD_secure(String d_secure) {
            this.d_secure = d_secure;
        }
    }
}
