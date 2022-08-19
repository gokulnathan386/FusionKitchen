package com.fusionkitchen.model.home_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class serachgetshop_modal {


    @SerializedName("STATUS")
    private String STATUS;


    @SerializedName("postcode")
    private String postcode;


    @SerializedName("location")
    private serachgetshop_modal.location location;

    @SerializedName("clientinfo")
    private List<serachgetshop_modal.clientinfos> clientinfo;

    @SerializedName("area")
    private String area;


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

    public serachgetshop_modal.location getLocation() {
        return location;
    }

    public void setLocation(serachgetshop_modal.location location) {
        this.location = location;
    }


    public List<clientinfos> getClientinfo() {
        return clientinfo;
    }

    public void setClientinfo(List<clientinfos> clientinfo) {
        this.clientinfo = clientinfo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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


    public class clientinfos {


        @SerializedName("bgimge")
        private String bgimge;
        @SerializedName("clientID")
        private String clientID;
        @SerializedName("clientName")
        private String clientName;
        @SerializedName("clientlogo")
        private String clientlogo;
        @SerializedName("deliverytime")
        private String deliverytime;
        @SerializedName("takeaway")
        private String takeaway;
        @SerializedName("delivery")
        private String delivery;
        @SerializedName("takeawayStautsDetails")
        private String takeawayStautsDetails;

        @SerializedName("favroite")
        private String favroite;


        @SerializedName("Rating")
        private String Rating;
        @SerializedName("menuurl")
        private String menuurl;

        @SerializedName("distance")
        private String distance;


        @SerializedName("offer")
        private List<serachgetshop_modal.clientinfos.offer> offer;


        @SerializedName("onlinediscount")
        private List<serachgetshop_modal.clientinfos.onlinediscount> onlinediscount;


        @SerializedName("client_cuisines")
        private List<serachgetshop_modal.clientinfos.client_cuisines> client_cuisines;

        @SerializedName("clientpath")
        private String clientpath;


        @SerializedName("path")
        private String path;



        public String getpath() {
            return path;
        }

        public void setpath(String path) {
            this.path = path;
        }


        public String getClientpath() {
            return clientpath;
        }

        public void setClientpath(String clientpath) {
            this.clientpath = clientpath;
        }


        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
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

        public String getTakeawayStautsDetails() {
            return takeawayStautsDetails;
        }

        public void setTakeawayStautsDetails(String takeawayStautsDetails) {
            this.takeawayStautsDetails = takeawayStautsDetails;
        }


        public String getFavroite() {
            return favroite;
        }

        public void setFavroite(String favroite) {
            this.favroite = favroite;
        }

        public String getRating() {
            return Rating;
        }

        public void setRating(String rating) {
            Rating = rating;
        }

        public String getMenuurl() {
            return menuurl;
        }

        public void setMenuurl(String menuurl) {
            this.menuurl = menuurl;
        }


        public List<clientinfos.offer> getOffer() {
            return offer;
        }

        public void setOffer(List<clientinfos.offer> offer) {
            this.offer = offer;
        }

        public List<clientinfos.onlinediscount> getOnlinediscount() {
            return onlinediscount;
        }

        public void setOnlinediscount(List<clientinfos.onlinediscount> onlinediscount) {
            this.onlinediscount = onlinediscount;
        }

        public List<clientinfos.client_cuisines> getClient_cuisines() {
            return client_cuisines;
        }

        public void setClient_cuisines(List<clientinfos.client_cuisines> client_cuisines) {
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
            @SerializedName("type")
            private String type;

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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public class onlinediscount {

            @SerializedName("discountimg")
            private String discountimg;

            @SerializedName("discount")
            private String discount;

            @SerializedName("discount_code")
            private String discount_code;

            @SerializedName("onlinedescription")
            private String onlinedescription;

            @SerializedName("type")
            private String type;


            public String getDiscountimg() {
                return discountimg;
            }

            public void setDiscountimg(String discountimg) {
                this.discountimg = discountimg;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getDiscount_code() {
                return discount_code;
            }

            public void setDiscount_code(String discount_code) {
                this.discount_code = discount_code;
            }

            public String getOnlinedescription() {
                return onlinedescription;
            }

            public void setOnlinedescription(String onlinedescription) {
                this.onlinedescription = onlinedescription;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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
}
