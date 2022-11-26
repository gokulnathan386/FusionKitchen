package com.fusionkitchen.model.menu_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.fusionkitchen.model.home_model.location_type_modal;
import com.fusionkitchen.model.home_model.location_type_sub_modal;

public class menu_item_model {

    //AccountResultModel
    @SerializedName("STATUS")
    private String STATUS;

    @SerializedName("topbanner")
    private List<menu_item_model.all_topbanner> topbanner;


    @SerializedName("menu")
    private menu_item_sub_model menu;

    @SerializedName("ordermode")
    private menu_item_sub_model.ordermodecs ordermode;


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public menu_item_sub_model getMenu() {
        return menu;
    }

    public void setMenu(menu_item_sub_model menu) {
        this.menu = menu;
    }


    public menu_item_sub_model.ordermodecs getOrdermode() {
        return ordermode;
    }

    public void setOrdermode(menu_item_sub_model.ordermodecs ordermode) {
        this.ordermode = ordermode;
    }

    public List<menu_item_model.all_topbanner> getTopbanner() {
        return topbanner;
    }

    public void setTopbanner(List<menu_item_model.all_topbanner> topbanner) {
        this.topbanner = topbanner;


    }

    //topbanner class
    public class all_topbanner {
        @SerializedName("clientName")
        private String clientName;

        @SerializedName("bg_image")
        private String bg_image;

        @SerializedName("reviews_count")
        private String reviews_count;

        @SerializedName("ratingValue")
        private String ratingValue;

        @SerializedName("delivery_time")
        private String delivery_time;
        @SerializedName("collection_time")
        private String collection_time;
        @SerializedName("minorder")
        private String minorder;

        @SerializedName("cuisinename")
        private List<menu_item_model.all_topbanner.cuisinename> cuisinename;

        @SerializedName("collection")
        private String collection;

        @SerializedName("delivery")
        private String delivery;


        @SerializedName("client_image")
        private String client_image;

        @SerializedName("miles")
        private String miles;

        @SerializedName("take_away_status")
        private TakeAwayStatus take_away_status;

        public TakeAwayStatus getTake_away_status() {
            return take_away_status;
        }

        public void setTake_away_status(TakeAwayStatus take_away_status) {
            this.take_away_status = take_away_status;
        }

        @SerializedName("images")
        private Images images;

        @SerializedName("client_id")
        private String client_id;

        public String getclient_id() {
            return client_id;
        }

        public void setclient_id(String client_id) {
            this.client_id = client_id;
        }


        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        public String getmiles() {
            return miles;
        }

        public void setmiles(String miles) {
            this.miles = miles;
        }

        public String getClienImage() {
            return client_image;
        }

        public void setClientImage(String client_image) {
            this.client_image = client_image;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getBg_image() {
            return bg_image;
        }

        public void setBg_image(String bg_image) {
            this.bg_image = bg_image;
        }


        public String getReviews_count() {
            return reviews_count;
        }

        public void setReviews_count(String reviews_count) {
            this.reviews_count = reviews_count;
        }

        public String getRatingValue() {
            return ratingValue;
        }

        public void setRatingValue(String ratingValue) {
            this.ratingValue = ratingValue;
        }


        public String getCollection_time() {
            return collection_time;
        }

        public void setCollection_time(String collection_time) {
            this.collection_time = collection_time;
        }

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

        public String getMinorder() {
            return minorder;
        }

        public void setMinorder(String minorder) {
            this.minorder = minorder;
        }

        public List<all_topbanner.cuisinename> getCuisinename() {
            return cuisinename;
        }

        public void setCuisinename(List<all_topbanner.cuisinename> cuisinename) {
            this.cuisinename = cuisinename;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public class cuisinename {

            @SerializedName("cuisine")
            private String cuisine;

            public String getCuisine() {
                return cuisine;
            }

            public void setCuisine(String cuisine) {
                this.cuisine = cuisine;
            }
        }

        public class TakeAwayStatus {

            @SerializedName("status_message")
            private String status_message;

            @SerializedName("status_detail")
            private String status_detail;


            public String getStatus_message() {
                return status_message;
            }

            public void setStatus_message(String status_message) {
                this.status_message = status_message;
            }

            public String getStatus_detail() {
                return status_detail;
            }

            public void setStatus_detail(String status_detail) {
                this.status_detail = status_detail;
            }
        }

        public class Images {
            private String repeat;
            private String delivery;
            private String walking;
            public String getRepeat() {
                return repeat;
            }
            public void setRepeat(String repeat) {
                this.repeat = repeat;
            }
            public String getDeliveryimage() {
                return delivery;
            }
            public void setDeliveryimage(String delivery1) {
                this.delivery = delivery1;
            }
            public String getWalking() {
                return walking;
            }
            public void setWalking(String walking) {
                this.walking = walking;
            }
        }


/*      public class take_away_status {

            private String status_message;
            private String status_detail;

            public String getstatus_message() {
                return status_message;
            }
            public void setstatus_message(String status_message) {
                this.status_message = status_message;
            }
            public String getstatus_detail() {
                return status_detail;
            }
            public void setstatus_detail(String status_detail) {
                this.status_detail = status_detail;
            }

        }*/


    }


}
