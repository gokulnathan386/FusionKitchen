package com.fusionkitchen.model.order_history;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class orderdetail_mode {


    @SerializedName("status")
    private String status;


    @SerializedName("reorder_details")
    private orderdetail_mode.reorder_details reorder_details;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public orderdetail_mode.reorder_details getReorder_details() {
        return reorder_details;
    }

    public void setReorder_details(orderdetail_mode.reorder_details reorder_details) {
        this.reorder_details = reorder_details;
    }



    public class reorder_details {


        @SerializedName("order")
        private orderdetail_mode.reorder_details.order order;


        public orderdetail_mode.reorder_details.order getOrder() {
            return order;
        }

        public void setOrder(orderdetail_mode.reorder_details.order order) {
            this.order = order;
        }

        public class order{



            @SerializedName("order")
            private order_details_list_show.order order;


            @SerializedName("user")
            private order_details_list_show.user user;


            @SerializedName("item")
            private List<order_details_list_show.item> item;


            public order_details_list_show.order getOrder() {
                return order;
            }

            public void setOrder(order_details_list_show.order order) {
                this.order = order;
            }

            public order_details_list_show.user getUser() {
                return user;
            }

            public void setUser(order_details_list_show.user user) {
                this.user = user;
            }


            public List<order_details_list_show.item> getItem() {
                return item;
            }

            public void setItem(List<order_details_list_show.item> item) {
                this.item = item;
            }
        }




    }
}


