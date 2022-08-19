package com.fusionkitchen.model.checkout;

import com.google.gson.annotations.SerializedName;

public class CheckloginModel {


    //AccountResultModel
    @SerializedName("status")
    private String status;


    @SerializedName("data")
    private CheckloginModel.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public CheckloginModel.data getData() {
        return data;
    }

    public void setData(CheckloginModel.data data) {
        this.data = data;
    }

    public class data {


        @SerializedName("order_id")
        private String order_id;
        @SerializedName("path")
        private String path;
        @SerializedName("orderdate")
        private String orderdate;
        @SerializedName("clientname")
        private String clientname;
        @SerializedName("clientid")
        private String clientid;
        @SerializedName("clientphonenumber")
        private String clientphonenumber;


        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getOrderdate() {
            return orderdate;
        }

        public void setOrderdate(String orderdate) {
            this.orderdate = orderdate;
        }

        public String getClientid() {
            return clientid;
        }

        public void setClientid(String clientid) {
            this.clientid = clientid;
        }

        public String getClientname() {
            return clientname;
        }

        public void setClientname(String clientname) {
            this.clientname = clientname;
        }

        public String getClientphonenumber() {
            return clientphonenumber;
        }

        public void setClientphonenumber(String clientphonenumber) {
            this.clientphonenumber = clientphonenumber;
        }
    }
}
