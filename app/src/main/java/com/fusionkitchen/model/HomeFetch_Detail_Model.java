package com.fusionkitchen.model;

import com.google.gson.annotations.SerializedName;

public class HomeFetch_Detail_Model {

    @SerializedName("STATUS")
    private Boolean STATUS;
    @SerializedName("RESPONSE_CODE")
    private Integer RESPONSE_CODE;

    @SerializedName("RESPONSE")
    private showToData RESPONSE;



    public Boolean getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(Boolean STATUS) {
        this.STATUS = STATUS;
    }

    public Integer getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    public void setRESPONSE_CODE(Integer RESPONSE_CODE) {
        this.RESPONSE_CODE = RESPONSE_CODE;
    }

    public showToData getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(showToData RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public class showToData{

        @SerializedName("backGroundImage")
        private String backGroundImage;

        @SerializedName("backGroundColor")
        private String backGroundColor;

        @SerializedName("frontImage")
        private String frontImage;

        @SerializedName("postDescribtion")
        private String postDescribtion;

        @SerializedName("postTextColor")
        private String postTextColor;

        @SerializedName("sloganDescribtion")
        private String sloganDescribtion;

        @SerializedName("sloganTextColor")
        private String sloganTextColor;

        public String getBackGroundImage() {
            return backGroundImage;
        }

        public void setBackGroundImage(String backGroundImage) {
            this.backGroundImage = backGroundImage;
        }

        public String getFrontImage() {
            return frontImage;
        }

        public void setFrontImage(String frontImage) {
            this.frontImage = frontImage;
        }

        public String getPostDescribtion() {
            return postDescribtion;
        }

        public void setPostDescribtion(String postDescribtion) {
            this.postDescribtion = postDescribtion;
        }

        public String getPostTextColor() {
            return postTextColor;
        }

        public void setPostTextColor(String postTextColor) {
            this.postTextColor = postTextColor;
        }

        public String getSloganDescribtion() {
            return sloganDescribtion;
        }

        public void setSloganDescribtion(String sloganDescribtion) {
            this.sloganDescribtion = sloganDescribtion;
        }

        public String getLoganTextColor() {
            return sloganTextColor;
        }

        public void setLoganTextColor(String sloganTextColor) {
            this.sloganTextColor = sloganTextColor;
        }

        public String getBackGroundColor() {
            return backGroundColor;
        }

        public void setBackGroundColor(String backGroundColor) {
            this.backGroundColor = backGroundColor;
        }

        public String getSloganTextColor() {
            return sloganTextColor;
        }

        public void setSloganTextColor(String sloganTextColor) {
            this.sloganTextColor = sloganTextColor;
        }
    }
}
