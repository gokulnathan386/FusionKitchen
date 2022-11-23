package com.fusionkitchen.model.offer;

import java.util.List;

public class offer_singe_List {

        private String promo_coupon_common_Id;
        private String free;
        private String type;
        private String discount;
        private String payment_details;
        private String min_order;
        private String order_type;
        private String valid_date;
        private String description;



    public offer_singe_List(String promo_coupon_common_Id, String free, String type, String discount,
                            String payment_details, String min_order, String order_type,
                            String valid_date, String description) {

            this.promo_coupon_common_Id = promo_coupon_common_Id;
            this.free = free;
            this.type =type;
            this.discount =discount;
            this.payment_details = payment_details;
            this.min_order = min_order;
            this.order_type =order_type;
            this.valid_date =valid_date;
            this.description = description;



          }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getPromo_coupon_common_Id() {
            return promo_coupon_common_Id;
        }

        public void setPromo_coupon_common_Id(String promo_coupon_common_Id) {
            this.promo_coupon_common_Id = promo_coupon_common_Id;
        }


    public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPayment_details() {
            return payment_details;
        }

        public void setPayment_details(String payment_details) {
            this.payment_details = payment_details;
        }

        public String getMin_order() {
            return min_order;
        }

        public void setMin_order(String min_order) {
            this.min_order = min_order;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getValid_date() {
            return valid_date;
        }

        public void setValid_date(String valid_date) {
            this.valid_date = valid_date;
        }
    }




