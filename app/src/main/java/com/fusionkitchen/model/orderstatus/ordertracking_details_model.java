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


        @SerializedName("order_dateword")
        private String order_dateword;

        @SerializedName("orderCount")
        private String orderCount;

        @SerializedName("pickup_latitude")
        private String pickup_latitude;

        @SerializedName("pickup_longitude")
        private String pickup_longitude;

        @SerializedName("dropoff_latitude")
        private String dropoff_latitude;

        @SerializedName("dropoff_longitude")
        private String dropoff_longitude;


        @SerializedName("driver_latitude")
        private String driver_latitude;

        @SerializedName("driver_longitude")
        private String driver_longitude;

        @SerializedName("delivery_status")
        private String delivery_status;

        @SerializedName("stuart_status")
        private String stuart_status;

        @SerializedName("delivery_status_name")
        private String delivery_status_name;

        @SerializedName("order_expected_time")
        private String order_expected_time;

        @SerializedName("driver_name")
        private String driver_name;

        @SerializedName("driver_number")
        private String driver_number;


        public String getdriver_number() {
            return driver_number;
        }

        public void setdriver_number(String driver_number) {
            this.driver_number = driver_number;
        }

        public String getdriver_name() {
            return driver_name;
        }

        public void setdriver_name(String driver_name) {
            this.driver_name = driver_name;
        }

        public String getorder_expected_time() {
            return order_expected_time;
        }

        public void setorder_expected_time(String order_expected_time) {
            this.order_expected_time = order_expected_time;
        }


        public String getdelivery_status_name() {
            return delivery_status_name;
        }

        public void setdelivery_status_name(String delivery_status_name) {
            this.delivery_status_name = delivery_status_name;
        }

        public String getstuart_status() {
            return stuart_status;
        }

        public void setstuart_status(String stuart_status) {
            this.stuart_status = stuart_status;
        }

        public String getdelivery_status() {
            return delivery_status;
        }

        public void setdelivery_status(String delivery_status) {
            this.delivery_status = delivery_status;
        }

        public String getdriver_longitude() {
            return driver_longitude;
        }

        public void setdriver_longitude(String driver_longitude) {
            this.driver_longitude = driver_longitude;
        }

        public String getdriver_latitude() {
            return driver_latitude;
        }

        public void setdriver_latitude(String driver_latitude) {
            this.driver_latitude = driver_latitude;
        }


        public String getdropoff_longitude() {
            return dropoff_longitude;
        }

        public void setdropoff_longitude(String dropoff_longitude) {
            this.dropoff_longitude = dropoff_longitude;
        }

        public String getdropoff_latitude() {
            return dropoff_latitude;
        }

        public void setdropoff_latitude(String dropoff_latitude) {
            this.dropoff_latitude = dropoff_latitude;
        }

        public String getpickup_latitude() {
            return pickup_latitude;
        }

        public void setpickup_latitude(String pickup_latitude) {
            this.pickup_latitude = pickup_latitude;
        }

        public String getpickup_longitude() {
            return pickup_longitude;
        }

        public void setpickup_longitude(String pickup_longitude) {
            this.pickup_longitude = pickup_longitude;
        }




        public String getorderCount() {
            return orderCount;
        }

        public void setorderCount(String orderCount) {
            this.orderCount = orderCount;
        }


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



        public String get_order_dateword() {
            return order_dateword;
        }

        public void set_order_dateword(String order_dateword) {
            this.order_dateword = order_dateword;
        }

    }

    public class user {

        @SerializedName("fname")
        private String fname;

        @SerializedName("lname")
        private String lname;

        @SerializedName("phone")
        private String phone;

        @SerializedName("dno")
        private String dno;

        @SerializedName("add1")
        private String add1;

        @SerializedName("add2")
        private String add2;

        @SerializedName("email")
        private String email;

        @SerializedName("postcode")
        private String postcode;



        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }


        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getadd1() {
            return add1;
        }

        public void setadd1(String add1) {
            this.add1 = add1;
        }
        public String getadd2() {
            return add2;
        }

        public void setadd2(String add2) {
            this.add2 = add2;
        }

        public String getemail() {
            return email;
        }

        public void setemail(String email) {
            this.email = email;
        }

        public String getdno() {
            return dno;
        }

        public void setdno(String dno) {
            this.dno = dno;
        }


        public String getpostcode() {
            return postcode;
        }

        public void setpostcode(String postcode) {
            this.postcode = postcode;
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

    public class rest {

        @SerializedName("name")
        private String name;

        @SerializedName("mobile")
        private  String mobile;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getmobile() {
            return mobile;
        }

        public void setmobile(String mobile) {
            this.mobile = mobile;
        }



    }


}
