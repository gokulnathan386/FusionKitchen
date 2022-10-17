package com.fusionkitchen.model.menu_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class search_menu_item_model {


    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<search_menu_item_model.data> data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<search_menu_item_model.data> getData() {
        return data;
    }

    public void setData(List<search_menu_item_model.data> data) {
        this.data = data;
    }

    public class data{


        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("price")
        private String price;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }


}
