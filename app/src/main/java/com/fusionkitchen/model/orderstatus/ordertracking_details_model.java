package com.fusionkitchen.model.orderstatus;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.fusionkitchen.model.order_history.order_details_list_show;

public class ordertracking_details_model {
    public class order {


        @SerializedName("total")
        private String total;
        @SerializedName("status")
        private String status;
        @SerializedName("otype")
        private String otype;
        @SerializedName("drivertracking")
        private String drivertracking;

        @SerializedName("sub_total")
        private String sub_total;
        @SerializedName("bank")
        private String bank;
        @SerializedName("bagage")
        private String bagage;
        @SerializedName("delivery_charge")
        private String delivery_charge;
        @SerializedName("discount")
        private String discount;

        @SerializedName("promo_discount")
        private String promo_discount;


        @SerializedName("order_date")
        private String order_date;

        @SerializedName("order_feedback")
        private String order_feedback;



        public String getorderfeedback() {
            return order_feedback;
        }

        public void setorderfeedback(String order_feedback) {
            this.order_feedback = order_feedback;
        }


        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOtype() {
            return otype;
        }

        public void setOtype(String otype) {
            this.otype = otype;
        }


        public String getDrivertracking() {
            return drivertracking;
        }

        public void setDrivertracking(String drivertracking) {
            this.drivertracking = drivertracking;
        }


        public String getSub_total() {
            return sub_total;
        }

        public void setSub_total(String sub_total) {
            this.sub_total = sub_total;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBagage() {
            return bagage;
        }

        public void setBagage(String bagage) {
            this.bagage = bagage;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPromo_discount() {
            return promo_discount;
        }

        public void setPromo_discount(String promo_discount) {
            this.promo_discount = promo_discount;
        }


        public String get_order_date() {
            return order_date;
        }

        public void set_order_date(String order_date) {
            this.order_date = order_date;
        }

    }

    public class user {

        @SerializedName("fname")
        private String fname;


        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

    }

    public class item {


        @SerializedName("item_name")
        private String item_name;

        @SerializedName("qty")
        private String qty;

        @SerializedName("total")
        private String total;


        @SerializedName("addon")
        private List<ordertracking_details_model.item.addonlist> addon;

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }


        public List<addonlist> getAddon() {
            return addon;
        }

        public void setAddon(List<addonlist> addon) {
            this.addon = addon;
        }

        public class addonlist {

            @SerializedName("addon_name")
            private String addon_name;

            @SerializedName("total")
            private String total;


            public String getAddon_name() {
                return addon_name;
            }

            public void setAddon_name(String addon_name) {
                this.addon_name = addon_name;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }
        }

    }
}
