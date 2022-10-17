package com.fusionkitchen.model.order_history;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.fusionkitchen.model.addon.menu_addons_model;


public class ordhistorys_list_model {


    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;


    @SerializedName("reorder_details")
    private List<ordhistorys_list_model.reorder_details> reorder_details;


    public List<ordhistorys_list_model.reorder_details> getReorder_details() {
        return reorder_details;
    }

    public void setReorder_details(List<ordhistorys_list_model.reorder_details> reorder_details) {
        this.reorder_details = reorder_details;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class reorder_details {

        @SerializedName("order")
        private ordhistorys_list_model.orderdetails order;

        @SerializedName("client")
        private ordhistorys_list_model.clientdetails client;


        public orderdetails getOrder() {
            return order;
        }

        public void setOrder(orderdetails order) {
            this.order = order;
        }

        public clientdetails getClient() {
            return client;
        }

        public void setClient(clientdetails client) {
            this.client = client;
        }
    }


    public class orderdetails {


        //AccountResultModel
        @SerializedName("id")
        private String id;

        @SerializedName("total")
        private String total;

        @SerializedName("otype")
        private String otype;


        @SerializedName("date")
        private String date;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getOtype() {
            return otype;
        }

        public void setOtype(String otype) {
            this.otype = otype;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public class clientdetails {

        @SerializedName("path")
        private String path;

        @SerializedName("cid")
        private String cid;

        @SerializedName("logo")
        private String logo;

        @SerializedName("clientname")
        private String clientname;


        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getClientname() {
            return clientname;
        }

        public void setClientname(String clientname) {
            this.clientname = clientname;
        }
    }
}
