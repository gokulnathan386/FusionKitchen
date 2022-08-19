package com.fusionkitchen.model.home_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class order_type_sub_modal {

    public class location {

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

    public class clients {


        @SerializedName("menuurlpath")
        private String menuurlpath;
        @SerializedName("clientpath")
        private String clientpath;
        @SerializedName("bgimge")
        private String bgimge;
        @SerializedName("clientID")
        private String clientID;
        @SerializedName("clientName")
        private String clientName;
        @SerializedName("client_cuisinesnew")
        private String client_cuisinesnew;
        @SerializedName("Rating")
        private String Rating;


        @SerializedName("opening")
        private String opening;
        @SerializedName("clientlogo")
        private String clientlogo;
        @SerializedName("deliverytime")
        private String deliverytime;
        @SerializedName("deliverymin")
        private String deliverymin;
        @SerializedName("takeaway")
        private String takeaway;
        @SerializedName("delivery")
        private String delivery;
        @SerializedName("preorder")
        private String preorder;
        @SerializedName("takeawaystatus")
        private String takeawaystatus;
        @SerializedName("status")
        private String status;

        @SerializedName("booknow")
        private String booknow;
        @SerializedName("menupth")
        private String menupth;
        @SerializedName("menuurl")
        private String menuurl;
        @SerializedName("ordernow")
        private String ordernow;


        @SerializedName("offer")
        private List<order_type_sub_modal.clients.offer> offer;
        @SerializedName("client_cuisines")
        private List<order_type_sub_modal.clients.client_cuisines> client_cuisines;


        public String getMenuurlpath() {
            return menuurlpath;
        }

        public void setMenuurlpath(String menuurlpath) {
            this.menuurlpath = menuurlpath;
        }


        public String getClientpath() {
            return clientpath;
        }

        public void setClientpath(String clientpath) {
            this.clientpath = clientpath;
        }

        public String getBgimge() {
            return bgimge;
        }

        public void setBgimge(String bgimge) {
            this.bgimge = bgimge;
        }

        public String getClientID() {
            return clientID;
        }

        public void setClientID(String clientID) {
            this.clientID = clientID;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getClient_cuisinesnew() {
            return client_cuisinesnew;
        }

        public void setClient_cuisinesnew(String client_cuisinesnew) {
            this.client_cuisinesnew = client_cuisinesnew;
        }

        public String getRating() {
            return Rating;
        }

        public void setRating(String rating) {
            Rating = rating;
        }

        public String getOpening() {
            return opening;
        }

        public void setOpening(String opening) {
            this.opening = opening;
        }

        public String getClientlogo() {
            return clientlogo;
        }

        public void setClientlogo(String clientlogo) {
            this.clientlogo = clientlogo;
        }

        public String getDeliverytime() {
            return deliverytime;
        }

        public void setDeliverytime(String deliverytime) {
            this.deliverytime = deliverytime;
        }

        public String getDeliverymin() {
            return deliverymin;
        }

        public void setDeliverymin(String deliverymin) {
            this.deliverymin = deliverymin;
        }

        public String getTakeaway() {
            return takeaway;
        }

        public void setTakeaway(String takeaway) {
            this.takeaway = takeaway;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getPreorder() {
            return preorder;
        }

        public void setPreorder(String preorder) {
            this.preorder = preorder;
        }

        public String getTakeawaystatus() {
            return takeawaystatus;
        }

        public void setTakeawaystatus(String takeawaystatus) {
            this.takeawaystatus = takeawaystatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBooknow() {
            return booknow;
        }

        public void setBooknow(String booknow) {
            this.booknow = booknow;
        }

        public String getMenupth() {
            return menupth;
        }

        public void setMenupth(String menupth) {
            this.menupth = menupth;
        }

        public String getMenuurl() {
            return menuurl;
        }

        public void setMenuurl(String menuurl) {
            this.menuurl = menuurl;
        }

        public String getOrdernow() {
            return ordernow;
        }

        public void setOrdernow(String ordernow) {
            this.ordernow = ordernow;
        }

        public List<order_type_sub_modal.clients.offer> getOffer() {
            return offer;
        }

        public void setOffer(List<order_type_sub_modal.clients.offer> offer) {
            this.offer = offer;
        }

        public List<order_type_sub_modal.clients.client_cuisines> getClient_cuisines() {
            return client_cuisines;
        }

        public void setClient_cuisines(List<order_type_sub_modal.clients.client_cuisines> client_cuisines) {
            this.client_cuisines = client_cuisines;
        }

        public class offer {


            @SerializedName("offerimg")
            private String offerimg;
            @SerializedName("prodiscount")
            private String prodiscount;
            @SerializedName("promocode")
            private String promocode;
            @SerializedName("promodescription")
            private String promodescription;


            public String getOfferimg() {
                return offerimg;
            }

            public void setOfferimg(String offerimg) {
                this.offerimg = offerimg;
            }

            public String getProdiscount() {
                return prodiscount;
            }

            public void setProdiscount(String prodiscount) {
                this.prodiscount = prodiscount;
            }

            public String getPromocode() {
                return promocode;
            }

            public void setPromocode(String promocode) {
                this.promocode = promocode;
            }

            public String getPromodescription() {
                return promodescription;
            }

            public void setPromodescription(String promodescription) {
                this.promodescription = promodescription;
            }
        }

        public class client_cuisines {


            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;


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
        }
    }

    public class all_cuisine {

        @SerializedName("image")
        private String image;
        @SerializedName("cuisinesID")
        private String cuisinesID;
        @SerializedName("name")
        private String name;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCuisinesID() {
            return cuisinesID;
        }

        public void setCuisinesID(String cuisinesID) {
            this.cuisinesID = cuisinesID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
