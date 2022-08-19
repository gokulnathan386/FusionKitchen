package com.fusionkitchen.model.home_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class order_type_modal {

    //AccountResultModel
    @SerializedName("STATUS")
    private String STATUS;


    @SerializedName("location")
    private String location;

    @SerializedName("locations")
    private String locations;

    @SerializedName("RESPONSE_CODE")
    private String RESPONSE_CODE;

    @SerializedName("msg")
    private String msg;


    @SerializedName("clientinfo")
    private order_type_modal.clientinfos clientinfo;


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    public void setRESPONSE_CODE(String RESPONSE_CODE) {
        this.RESPONSE_CODE = RESPONSE_CODE;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public clientinfos getClientinfo() {
        return clientinfo;
    }

    public void setClientinfo(clientinfos clientinfo) {
        this.clientinfo = clientinfo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class clientinfos {

        @SerializedName("postcode")
        private String postcode;

        @SerializedName("area")
        private String area;

        @SerializedName("location")
        private order_type_sub_modal.location location;

        @SerializedName("clients")
        private List<order_type_sub_modal.clients> clients;

        @SerializedName("all_cuisine")
        private List<order_type_sub_modal.all_cuisine> all_cuisine;

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public order_type_sub_modal.location getLocation() {
            return location;
        }

        public void setLocation(order_type_sub_modal.location location) {
            this.location = location;
        }


        public List<order_type_sub_modal.clients> getClients() {
            return clients;
        }

        public void setClients(List<order_type_sub_modal.clients> clients) {
            this.clients = clients;
        }

        public List<order_type_sub_modal.all_cuisine> getAll_cuisine() {
            return all_cuisine;
        }

        public void setAll_cuisine(List<order_type_sub_modal.all_cuisine> all_cuisine) {
            this.all_cuisine = all_cuisine;
        }
    }
}
