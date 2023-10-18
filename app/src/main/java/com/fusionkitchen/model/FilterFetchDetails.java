package com.fusionkitchen.model;

import com.fusionkitchen.model.dashboard.location_fetch_details;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilterFetchDetails {

    @SerializedName("status")
    private Boolean status;

    @SerializedName("data")
    private dateShow data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public dateShow getData() {
        return data;
    }

    public void setData(dateShow data) {
        this.data = data;
    }

    public  class dateShow{


        @SerializedName("restaurantList")
        private List<showRestaurantist> restaurantList;

        public List<showRestaurantist> getRestaurantList() {
            return restaurantList;
        }

        public void setRestaurantList(List<showRestaurantist> restaurantList) {
            this.restaurantList = restaurantList;
        }

    }


    public  class showRestaurantist{
        @SerializedName("restaurant")
        private restaurantlist restaurantList;

        public restaurantlist getRestaurantList() {
            return restaurantList;
        }

        public void setRestaurantList(restaurantlist restaurantList) {
            this.restaurantList = restaurantList;
        }
    }

    public class restaurantlist{

        @SerializedName("backgroundImage")
        private String  backgroundImage;

        @SerializedName("name")
        private String  name;

        @SerializedName("miles")
        private String  miles;

        @SerializedName("cookingTimeStart")
        private String  cookingTimeStart;

        @SerializedName("cookingTimeEnd")
        private String  cookingTimeEnd;

        @SerializedName("rating")
        private location_fetch_details.RatingDetails rating;


        @SerializedName("restaurantStatus")
        private location_fetch_details.TakewayCloseOpenStatus restaurantStatus;

        @SerializedName("vip")
        private Boolean vip;

        @SerializedName("discount")
        private location_fetch_details.Discount discount;

        @SerializedName("cuisineName")
        private ArrayList<String> cuisineName;

        public ArrayList<String> getCuisineName() {
            return cuisineName;
        }

        public void setCuisineName(ArrayList<String> cuisineName) {
            this.cuisineName = cuisineName;
        }

        public location_fetch_details.TakewayCloseOpenStatus getRestaurantStatus() {
            return restaurantStatus;
        }

        public void setRestaurantStatus(location_fetch_details.TakewayCloseOpenStatus restaurantStatus) {
            this.restaurantStatus = restaurantStatus;
        }

        public Boolean getVip() {
            return vip;
        }

        public void setVip(Boolean vip) {
            this.vip = vip;
        }

        public String getBackgroundImage() {
            return backgroundImage;
        }

        public void setBackgroundImage(String backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMiles() {
            return miles;
        }

        public void setMiles(String miles) {
            this.miles = miles;
        }

        public String getCookingTimeStart() {
            return cookingTimeStart;
        }

        public void setCookingTimeStart(String cookingTimeStart) {
            this.cookingTimeStart = cookingTimeStart;
        }

        public String getCookingTimeEnd() {
            return cookingTimeEnd;
        }

        public void setCookingTimeEnd(String cookingTimeEnd) {
            this.cookingTimeEnd = cookingTimeEnd;
        }

        public location_fetch_details.RatingDetails getRating() {
            return rating;
        }

        public void setRating(location_fetch_details.RatingDetails rating) {
            this.rating = rating;
        }

        public location_fetch_details.Discount getDiscount() {
            return discount;
        }

        public void setDiscount(location_fetch_details.Discount discount) {
            this.discount = discount;
        }

    }
}
