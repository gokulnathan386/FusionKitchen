package com.fusionkitchen.model.orderstatus;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ordertracking_model {

    @SerializedName("status")
    private String status;


    @SerializedName("ordertracking")
    private ordertracking_model.ordertracking ordertracking;




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ordertracking_model.ordertracking getOrdertracking() {
        return ordertracking;
    }

    public void setOrdertracking(ordertracking_model.ordertracking ordertracking) {
        this.ordertracking = ordertracking;
    }

    public class ordertracking{

        @SerializedName("order")
        private ordertracking_model.ordertracking.order order;

        @SerializedName("clientsdetails")
        private ordertracking_details_model.clientsdetails clientsdetails;


        public ordertracking_model.ordertracking.order getOrder() {
            return order;
        }

        public void setOrder(ordertracking_model.ordertracking.order order) {
            this.order = order;
        }


        public ordertracking_details_model.clientsdetails getClientsdetails() {
            return clientsdetails;
        }

        public void setClientsdetails(ordertracking_details_model.clientsdetails clientsdetails) {
            this.clientsdetails = clientsdetails;
        }







        public class order{



            @SerializedName("order")
            private ordertracking_details_model.order order;


            @SerializedName("user")
            private ordertracking_details_model.user  user;


            @SerializedName("item")
            private List<ordertracking_details_model.item> item;


            @SerializedName("rest")
            private ordertracking_details_model.rest rest;


            public ordertracking_details_model.order getOrder() {
                return order;
            }

            public void setOrder(ordertracking_details_model.order order) {
                this.order = order;
            }

            public List<ordertracking_details_model.item> getItem() {
                return item;
            }

            public void setItem(List<ordertracking_details_model.item> item) {
                this.item = item;
            }

            public ordertracking_details_model.user getUser() {
                return user;
            }

            public void setUser(ordertracking_details_model.user user) {
                this.user = user;
            }


            public ordertracking_details_model.rest getrest() {
                return rest;
            }

            public void setrest(ordertracking_details_model.rest rest) {
                this.rest =  rest;
            }
        }

    }
}
