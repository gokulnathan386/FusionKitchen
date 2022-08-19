package com.fusionkitchen.model.order_history;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class reorderdetail_mode {

    @SerializedName("status")
    private String status;

    @SerializedName("reorder_details")
    private reorderdetail_mode.reorder_details reorder_details;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public reorderdetail_mode.reorder_details getReorder_details() {
        return reorder_details;
    }

    public void setReorder_details(reorderdetail_mode.reorder_details reorder_details) {
        this.reorder_details = reorder_details;
    }

    public class reorder_details {

        @SerializedName("order")
        private reorderdetail_mode.orderdetails order;

        public orderdetails getOrder() {
            return order;
        }

        public void setOrder(orderdetails order) {
            this.order = order;
        }
    }

    public class orderdetails {
        @SerializedName("item")
        private List<reorder_details_show.item> item;

        public List<reorder_details_show.item> getItem() {
            return item;
        }

        public void setItem(List<reorder_details_show.item> item) {
            this.item = item;
        }
    }
}
