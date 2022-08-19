package com.fusionkitchen.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class version_code_modal {

    //AccountResultModel
    @SerializedName("status")
    private String status;


    @SerializedName("version")
    private List<version_code_modal.version> version;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<version_code_modal.version> getVersion() {
        return version;
    }

    public void setVersion(List<version_code_modal.version> version) {
        this.version = version;
    }

    public class version{

        @SerializedName("version")
        private String version;

        @SerializedName("app_url")
        private String app_url;


        @SerializedName("sociallogin")
        private String sociallogin;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getApp_url() {
            return app_url;
        }

        public void setApp_url(String app_url) {
            this.app_url = app_url;
        }

        public String getSociallogin() {
            return sociallogin;
        }

        public void setSociallogin(String sociallogin) {
            this.sociallogin = sociallogin;
        }
    }
}
