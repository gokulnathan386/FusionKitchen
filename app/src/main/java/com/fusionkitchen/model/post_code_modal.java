package com.fusionkitchen.model;

import com.google.gson.annotations.SerializedName;

public class post_code_modal {


    //AccountResultModel
    @SerializedName("STATUS")
    private String STATUS;
    @SerializedName("postcode")
    private String postcode;
    @SerializedName("url")
    private String url;
    @SerializedName("fullpostcode")
    private String fullpostcode;
    @SerializedName("address")
    private String address;
    @SerializedName("area")
    private String area;

    @SerializedName("location")
    private post_code_modal.location location;


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFullpostcode() {
        return fullpostcode;
    }

    public void setFullpostcode(String fullpostcode) {
        this.fullpostcode = fullpostcode;
    }

    public post_code_modal.location getLocation() {
        return location;
    }

    public void setLocation(post_code_modal.location location) {
        this.location = location;
    }

    public class location{


        @SerializedName("lat")
        private String lat;
        @SerializedName("lng")
        private String lng;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }
}
