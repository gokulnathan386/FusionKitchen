package com.fusionkitchen.model.dashboard;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchFilterListModel {


    @SerializedName("status")
    private Boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ShowToDate data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public ShowToDate getData() {
        return data;
    }

    public void setData(ShowToDate data) {
        this.data = data;
    }


    public class ShowToDate{

        @SerializedName("getAllActiveCuisine")
        private List<GetAllCuisinefilter> getAllActiveCuisine;

        public List<GetAllCuisinefilter> getGetAllActiveCuisine() {
            return getAllActiveCuisine;
        }

        public void setGetAllActiveCuisine(List<GetAllCuisinefilter> getAllActiveCuisine) {
            this.getAllActiveCuisine = getAllActiveCuisine;
        }
    }


    public class GetAllCuisinefilter{

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("count")
        private String count;

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

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
