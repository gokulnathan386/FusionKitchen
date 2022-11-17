package com.fusionkitchen.model.offer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.fusionkitchen.model.addon.menu_addon_status_model;
import com.fusionkitchen.model.addon.menu_addons_model;

public class offer_code_model {


    @SerializedName("status")
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @SerializedName("discount_list")
    private offer_code_model.discount_list discount_list;


    public offer_code_model.discount_list getDiscount_list() {
        return discount_list;
    }

    public void setDiscount_list(offer_code_model.discount_list discount_list) {
        this.discount_list = discount_list;
    }

    public class discount_list{

        @SerializedName("promocode")
        private List<offer_list_model_details.promocode> promocode;


        @SerializedName("discountcode")
        private List<offer_list_model_details.discountcode> discountcode;



        @SerializedName("commoncoupon")
        private List<offer_list_model_details.commoncoupon> commoncoupon;

        public List<offer_list_model_details.discountcode> getDiscountcode() {
            return discountcode;
        }

        public void setDiscountcode(List<offer_list_model_details.discountcode> discountcode) {
            this.discountcode = discountcode;
        }


        public List<offer_list_model_details.promocode> getPromocode() {
            return promocode;
        }

        public void setPromocode(List<offer_list_model_details.promocode> promocode) {
            this.promocode = promocode;
        }


        public List<offer_list_model_details.commoncoupon> getCommoncoupon() {
            return commoncoupon;
        }

        public void setCommoncoupon(List<offer_list_model_details.commoncoupon> commoncoupon) {
            this.commoncoupon = commoncoupon;
        }


    }


}
