package com.fusionkitchen.model.cart;

import com.google.gson.annotations.SerializedName;

public class subcategory_printer_model {


    @SerializedName("subcategory")
    private String subcategory;


    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
