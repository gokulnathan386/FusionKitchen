package com.fusionkitchen.model.order_history;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class order_details_list_show {

    public class order {


        @SerializedName("sub_total")
        private String sub_total;

        @SerializedName("discount")
        private String discount;

        @SerializedName("bank")
        private String bank;

        @SerializedName("total")
        private String total;

        @SerializedName("delivery_charge")
        private String delivery_charge;

        @SerializedName("bagage")
        private String bagage;


        @SerializedName("promo_discount")
        private String promo_discount;


        public String getSub_total() {
            return sub_total;
        }

        public void setSub_total(String sub_total) {
            this.sub_total = sub_total;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }


        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getBagage() {
            return bagage;
        }

        public void setBagage(String bagage) {
            this.bagage = bagage;
        }

        public String getPromo_discount() {
            return promo_discount;
        }

        public void setPromo_discount(String promo_discount) {
            this.promo_discount = promo_discount;
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

        @SerializedName("total")
        private String total;

        @SerializedName("price")
        private String price;

        @SerializedName("qty")
        private String qty;

        @SerializedName("itemid")
        private String itemid;

        @SerializedName("sub_name")
        private String sub_name;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @SerializedName("addon")
        private List<order_details_list_show.item.addonlist> addon;

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public List<addonlist> getAddon() {
            return addon;
        }

        public void setAddon(List<addonlist> addon) {
            this.addon = addon;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getSub_name() {
            return sub_name;
        }

        public void setSub_name(String sub_name) {
            this.sub_name = sub_name;
        }

        public class addonlist {

            @SerializedName("addon_name")
            private String addon_name;

            @SerializedName("total")
            private String total;

            @SerializedName("addonid")
            private String addonid;

            @SerializedName("price")
            private String price;

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

            public String getAddonid() {
                return addonid;
            }

            public void setAddonid(String addonid) {
                this.addonid = addonid;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }


}
