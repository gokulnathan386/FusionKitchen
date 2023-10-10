package com.fusionkitchen.model.dashboard;

import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class location_fetch_details {

    @SerializedName("status")
    private Boolean STATUS;

    @SerializedName("data")
    private dateShow  data;

    public Boolean getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(Boolean STATUS) {
        this.STATUS = STATUS;
    }

    public dateShow getDate() {
        return data;
    }

    public void setData(dateShow data) {
        this.data = data;
    }



    public class dateShow{

        @SerializedName("restaurantList")
        private List<showRestaurantist> restaurantList;

        @SerializedName("getAllActiveCuisine")
        private List<showCuisine> getAllActiveCuisine;

        public List<showCuisine> getGetAllActiveCuisine() {
            return getAllActiveCuisine;
        }

        public void setGetAllActiveCuisine(List<showCuisine> getAllActiveCuisine) {
            this.getAllActiveCuisine = getAllActiveCuisine;
        }

        public List<showRestaurantist> getRestaurantList() {
            return restaurantList;
        }

        public void setRestaurantList(List<showRestaurantist> restaurantList) {
            this.restaurantList = restaurantList;
        }
    }

    public class showRestaurantist{

        @SerializedName("restaurant")
        private restaurantList restaurant;

        public restaurantList getRestaurant() {
            return restaurant;
        }

        public void setRestaurant(restaurantList restaurant) {
            this.restaurant = restaurant;
        }
    }

    public class restaurantList{

        @SerializedName("backgroundImage")
        private String  backgroundImage;

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
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
