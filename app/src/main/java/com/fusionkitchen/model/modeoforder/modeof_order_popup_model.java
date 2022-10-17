package com.fusionkitchen.model.modeoforder;

import com.fusionkitchen.model.home_model.location_type_modal;
import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.fusionkitchen.model.menu_model.collDelivery_model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class modeof_order_popup_model {


    @SerializedName("status")
    private String status;


    @SerializedName("data")
    private modeof_order_popup_model.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public modeof_order_popup_model.data getData() {
        return data;
    }

    public void setData(modeof_order_popup_model.data data) {
        this.data = data;
    }

    public class data {


        @SerializedName("collection")
        private modeof_order_popup_model.data.collection collection;

        @SerializedName("delivery")
        private modeof_order_popup_model.data.delivery delivery;

        @SerializedName("message")
        private String message;


        public modeof_order_popup_model.data.collection getCollection() {
            return collection;
        }

        public void setCollection(modeof_order_popup_model.data.collection collection) {
            this.collection = collection;
        }

        public modeof_order_popup_model.data.delivery getDelivery() {
            return delivery;
        }

        public void setDelivery(modeof_order_popup_model.data.delivery delivery) {
            this.delivery = delivery;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public class collection {

            @SerializedName("asap")
            private modeof_order_popup_model.data.collection.asap asap;
            @SerializedName("today")
            private modeof_order_popup_model.data.collection.today today;
            @SerializedName("later_array")
            private modeof_order_popup_model.data.collection.later_array later_array;
            @SerializedName("status")
            private String status;
            @SerializedName("cooking_time")
            private String cooking_time;


            public modeof_order_popup_model.data.collection.asap getAsap() {
                return asap;
            }

            public void setAsap(modeof_order_popup_model.data.collection.asap asap) {
                this.asap = asap;
            }

            public modeof_order_popup_model.data.collection.today getToday() {
                return today;
            }

            public void setToday(modeof_order_popup_model.data.collection.today today) {
                this.today = today;
            }

            public modeof_order_popup_model.data.collection.later_array getLater_array() {
                return later_array;
            }

            public void setLater_array(modeof_order_popup_model.data.collection.later_array later_array) {
                this.later_array = later_array;
            }

            public String getCooking_time() {
                return cooking_time;
            }

            public void setCooking_time(String cooking_time) {
                this.cooking_time = cooking_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public class asap {

                @SerializedName("status")
                private String status;
                @SerializedName("asap_time")
                private String asap_time;
                @SerializedName("message")
                private String message;


                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getAsap_time() {
                    return asap_time;
                }

                public void setAsap_time(String asap_time) {
                    this.asap_time = asap_time;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }
            }


            public class today {

                @SerializedName("status")
                private String status;

                @SerializedName("message")
                private String message;


                @SerializedName("today_time")
                private List<modeof_order_popup_model.data.collection.today.today_time> today_time;




                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                public List<modeof_order_popup_model.data.collection.today.today_time> getToday_time() {
                    return today_time;
                }

                public void setToday_time(List<modeof_order_popup_model.data.collection.today.today_time> today_time) {
                    this.today_time = today_time;
                }

                public class today_time {

                    @SerializedName("today_time")
                    private String today_time;

                    @SerializedName("today_time_string")
                    private String today_time_string;

                    @SerializedName("today_label")
                    private String today_label;


                    public String getToday_time() {
                        return today_time;
                    }

                    public void setToday_time(String today_time) {
                        this.today_time = today_time;
                    }


                    public String getToday_time_string() {
                        return today_time_string;
                    }

                    public void setToday_time_string(String today_time_string) {
                        this.today_time_string = today_time_string;
                    }


                    public String gettoday_label() {
                        return today_label;
                    }

                    public void settoday_label(String today_label) {
                        this.today_label = today_label;
                    }

                }
            }

            public class later_array {

                @SerializedName("status")
                private String status;

                @SerializedName("message")
                private String message;


                @SerializedName("later_date")
                private List<modeof_order_popup_model.data.collection.later_array.later_date> later_date;


                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                public List<modeof_order_popup_model.data.collection.later_array.later_date> getLater_date() {
                    return later_date;
                }

                public void setLater_date(List<modeof_order_popup_model.data.collection.later_array.later_date> later_date) {
                    this.later_date = later_date;
                }

                public class later_date {

                    @SerializedName("later_date")
                    private String later_date;


                    public String getLater_date() {
                        return later_date;
                    }

                    public void setLater_date(String later_date) {
                        this.later_date = later_date;
                    }
                }
            }
        }

        public class delivery {


            @SerializedName("asap")
            private modeof_order_popup_model.data.delivery.asap asap;
            @SerializedName("today")
            private modeof_order_popup_model.data.delivery.today today;
            @SerializedName("later_array")
            private modeof_order_popup_model.data.delivery.later_array later_array;
            @SerializedName("status")
            private String status;
            @SerializedName("cooking_time")
            private String cooking_time;


            public modeof_order_popup_model.data.delivery.asap getAsap() {
                return asap;
            }

            public void setAsap(modeof_order_popup_model.data.delivery.asap asap) {
                this.asap = asap;
            }

            public modeof_order_popup_model.data.delivery.today getToday() {
                return today;
            }

            public void setToday(modeof_order_popup_model.data.delivery.today today) {
                this.today = today;
            }

            public modeof_order_popup_model.data.delivery.later_array getLater_array() {
                return later_array;
            }

            public void setLater_array(modeof_order_popup_model.data.delivery.later_array later_array) {
                this.later_array = later_array;
            }

            public String getCooking_time() {
                return cooking_time;
            }

            public void setCooking_time(String cooking_time) {
                this.cooking_time = cooking_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public class asap {

                @SerializedName("status")
                private String status;
                @SerializedName("asap_time")
                private String asap_time;
                @SerializedName("message")
                private String message;


                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getAsap_time() {
                    return asap_time;
                }

                public void setAsap_time(String asap_time) {
                    this.asap_time = asap_time;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }


            }

            public class today {

                @SerializedName("status")
                private String status;

                @SerializedName("message")
                private String message;


                @SerializedName("today_time")
                private List<modeof_order_popup_model.data.delivery.today.today_time> today_time;


                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                public List<modeof_order_popup_model.data.delivery.today.today_time> getToday_time() {
                    return today_time;
                }

                public void setToday_time(List<modeof_order_popup_model.data.delivery.today.today_time> today_time) {
                    this.today_time = today_time;
                }






                public class today_time {

                    @SerializedName("today_time")
                    private String today_time;

                    @SerializedName("today_time_string")
                    private String today_time_string;

                    @SerializedName("today_label")
                    private String today_label;

                    public String getToday_time() {
                        return today_time;
                    }

                    public void setToday_time(String today_time) {
                        this.today_time = today_time;
                    }


                    public String getToday_time_string() {
                        return today_time_string;
                    }

                    public void setToday_time_string(String today_time_string) {
                        this.today_time_string = today_time_string;
                    }


                    public String gettoday_label() {
                        return today_label;
                    }

                    public void settoday_label(String today_label) {
                        this.today_label = today_label;
                    }



                }
            }

            public class later_array {


                @SerializedName("status")
                private String status;

                @SerializedName("message")
                private String message;


                @SerializedName("later_date")
                private List<modeof_order_popup_model.data.delivery.later_array.later_date> later_date;


                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getMessage() {
                    return message;
                }

                public void setMessage(String message) {
                    this.message = message;
                }

                public List<modeof_order_popup_model.data.delivery.later_array.later_date> getLater_date() {
                    return later_date;
                }

                public void setLater_date(List<modeof_order_popup_model.data.delivery.later_array.later_date> later_date) {
                    this.later_date = later_date;
                }

                public class later_date {

                    @SerializedName("later_date")
                    private String later_date;


                    public String getLater_date() {
                        return later_date;
                    }

                    public void setLater_date(String later_date) {
                        this.later_date = later_date;
                    }
                }


            }
        }
    }
}
