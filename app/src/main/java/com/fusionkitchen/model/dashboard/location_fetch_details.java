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

        @SerializedName("offerBannerDetails")
        private List<BannerImage> offerBannerDetails;

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

        public List<BannerImage> getOfferBannerDetails() {
            return offerBannerDetails;
        }

        public void setOfferBannerDetails(List<BannerImage> offerBannerDetails) {
            this.offerBannerDetails = offerBannerDetails;
        }
    }

    public class BannerImage{

        @SerializedName("id")
        private String  id;

        @SerializedName("name")
        private String  name;

        @SerializedName("image")
        private String  image;


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

        @SerializedName("name")
        private String  name;

        @SerializedName("miles")
        private String  miles;

        @SerializedName("cookingTimeStart")
        private String  cookingTimeStart;

        @SerializedName("cookingTimeEnd")
        private String  cookingTimeEnd;

        @SerializedName("rating")
        private RatingDetails rating;


        @SerializedName("restaurantStatus")
        private TakewayCloseOpenStatus restaurantStatus;

        @SerializedName("vip")
        private Boolean vip;

        @SerializedName("discount")
        private Discount discount;



        public TakewayCloseOpenStatus getRestaurantStatus() {
            return restaurantStatus;
        }

        public void setRestaurantStatus(TakewayCloseOpenStatus restaurantStatus) {
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

        public RatingDetails getRating() {
            return rating;
        }

        public void setRating(RatingDetails rating) {
            this.rating = rating;
        }

        public Discount getDiscount() {
            return discount;
        }

        public void setDiscount(Discount discount) {
            this.discount = discount;
        }
    }

    public class Discount{

        @SerializedName("description")
        private String description;

        @SerializedName("discountType")
        private String discountType;

        @SerializedName("discount")
        private String discount;

        @SerializedName("minOrder")
        private String minOrder;


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDiscountType() {
            return discountType;
        }

        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getMinOrder() {
            return minOrder;
        }

        public void setMinOrder(String minOrder) {
            this.minOrder = minOrder;
        }
    }


    public class TakewayCloseOpenStatus{

        @SerializedName("status")
        private String status;

        @SerializedName("msg")
        private String msg;

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
    }


    public class RatingDetails{

        @SerializedName("rate")
        private String rate;

        @SerializedName("count")
        private String count;

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
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
