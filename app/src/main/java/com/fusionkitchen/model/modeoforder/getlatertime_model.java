package com.fusionkitchen.model.modeoforder;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class getlatertime_model {


    @SerializedName("data")
    private getlatertime_model.data data;


    public getlatertime_model.data getData() {
        return data;
    }

    public void setData(getlatertime_model.data data) {
        this.data = data;
    }

    public class data {



        @SerializedName("later_time")
        private List<getlatertime_model.data.later_time> later_time;



        @SerializedName("status")
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public List<getlatertime_model.data.later_time> getLater_time() {
            return later_time;
        }

        public void setLater_time(List<getlatertime_model.data.later_time> later_time) {
            this.later_time = later_time;
        }

        public class later_time {


            @SerializedName("later_time")
            private String later_time;

            @SerializedName("later_time_string")
            private String later_time_string;




            public String getLater_time() {
                return later_time;
            }

            public void setLater_time(String later_time) {
                this.later_time = later_time;
            }

            public String getLater_time_string() {
                return later_time_string;
            }

            public void setLater_time_string(String later_time_string) {
                this.later_time_string = later_time_string;
            }
        }
    }
}
