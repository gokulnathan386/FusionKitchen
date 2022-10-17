package com.fusionkitchen.model.offer;

import com.google.gson.annotations.SerializedName;

public class offer_list_model_details {
    public class discountcode {


        //AccountResultModel
        @SerializedName("free")
        private String free;

        @SerializedName("type")
        private String type;

        @SerializedName("discount")
        private String discount;

        @SerializedName("payment_details")
        private String payment_details;

        @SerializedName("min_order")
        private String min_order;


        @SerializedName("order_type")
        private String order_type;

        @SerializedName("valid_date")
        private String valid_date;

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

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
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

        public String getValid_date() {
            return valid_date;
        }

        public void setValid_date(String valid_date) {
            this.valid_date = valid_date;
        }
    }

    public class promocode {


        @SerializedName("free")
        private String free;

        @SerializedName("type")
        private String type;

        @SerializedName("discount")
        private String discount;


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
    }
}
