package com.fusionkitchen.model.orderstatus;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class orderstatus_model {


    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("orderstatus")
    private List<orderstatus_model.orderstatus> orderstatus;


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

    public List<orderstatus_model.orderstatus> getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(List<orderstatus_model.orderstatus> orderstatus) {
        this.orderstatus = orderstatus;
    }

    public class orderstatus {


        @SerializedName("order")
        private orderstatus_model.orderstatus.order order;

        @SerializedName("client")
        private orderstatus_model.orderstatus.client client;


        public orderstatus_model.orderstatus.order getOrder() {
            return order;
        }

        public void setOrder(orderstatus_model.orderstatus.order order) {
            this.order = order;
        }

        public orderstatus_model.orderstatus.client getClient() {
            return client;
        }

        public void setClient(orderstatus_model.orderstatus.client client) {
            this.client = client;
        }

        public class order {


            @SerializedName("id")
            private String id;

            @SerializedName("total")
            private String total;

            @SerializedName("status")
            private String status;

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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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

        public class client {
            @SerializedName("path")
            private String path;

            @SerializedName("logo")
            private String logo;

            @SerializedName("cid")
            private String cid;

            @SerializedName("clientname")
            private String clientname;

            @SerializedName("clientno")
            private String clientno;


            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getClientname() {
                return clientname;
            }

            public void setClientname(String clientname) {
                this.clientname = clientname;
            }

            public String getClientno() {
                return clientno;
            }

            public void setClientno(String clientno) {
                this.clientno = clientno;
            }
        }

    }


}
