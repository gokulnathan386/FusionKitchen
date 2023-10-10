package com.fusionkitchen.model.dashboard;

import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class location_fetch_details {

    @SerializedName("STATUS")
    private String STATUS;

    @SerializedName("data")
    private dateShow  data;

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public dateShow getDate() {
        return data;
    }

    public void setData(dateShow data) {
        this.data = data;
    }



    public class dateShow{


        @SerializedName("getAllActiveCuisine")
        private List<showCuisine> getAllActiveCuisine;

        public List<showCuisine> getGetAllActiveCuisine() {
            return getAllActiveCuisine;
        }

        public void setGetAllActiveCuisine(List<showCuisine> getAllActiveCuisine) {
            this.getAllActiveCuisine = getAllActiveCuisine;
        }
    }


    public class showCuisine{

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("image")
        private String image;

        @SerializedName("url")
        private String url;

        @SerializedName("client")
        private String client;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }
    }




}
