package com.fusionkitchen.model.address;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class getaddAddress_mode {

    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("userdetail")
    private List<getaddAddress_mode.userdetail> userdetail;


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


    public List<getaddAddress_mode.userdetail> getUserdetail() {
        return userdetail;
    }

    public void setUserdetail(List<getaddAddress_mode.userdetail> userdetail) {
        this.userdetail = userdetail;
    }

    public class userdetail {
        @SerializedName("id")
        private String id;
        @SerializedName("no")
        private String no;
        @SerializedName("address1")
        private String address1;
        @SerializedName("address2")
        private String address2;
        @SerializedName("postcode")
        private String postcode;
        @SerializedName("type")
        private String type;
        @SerializedName("cid")
        private String cid;
        @SerializedName("primary_address")
        private String primary_address;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getPrimary_address() {
            return primary_address;
        }

        public void setPrimary_address(String primary_address) {
            this.primary_address = primary_address;
        }


        public boolean isSelected() {
            return false;
        }

        public void setSelected(boolean b) {

        }
    }
}
