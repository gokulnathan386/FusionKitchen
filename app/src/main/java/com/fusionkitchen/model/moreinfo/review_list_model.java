package com.fusionkitchen.model.moreinfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class review_list_model {


    //AccountResultModel
    @SerializedName("STATUS")
    private String STATUS;


    @SerializedName("review")
    private List<review_list_model.review_list> review;


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public List<review_list> getReview() {
        return review;
    }

    public void setReview(List<review_list> review) {
        this.review = review;
    }

    public class review_list {


        //AccountResultModel
        @SerializedName("name")
        private String name;

        @SerializedName("rate")
        private String rate;

        @SerializedName("datetime")
        private String datetime;

        @SerializedName("comment")
        private String comment;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
