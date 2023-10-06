package com.fusionkitchen.model.dashboard;

import com.fusionkitchen.model.home_model.location_type_sub_modal;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class location_fetch_details {

    @SerializedName("STATUS")
    private String STATUS;

    @SerializedName("clientinfo")
    private ClientInfoDetails  clientinfo;

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public ClientInfoDetails getClientinfo() {
        return clientinfo;
    }

    public void setClientinfo(ClientInfoDetails clientinfo) {
        this.clientinfo = clientinfo;
    }


    public class ClientInfoDetails{

        @SerializedName("all_cuisine")
        private List<AllCuisineDetails> all_cuisine;

        @SerializedName("clients")
        private List<AllClientRestDetails> clients;

        public List<AllCuisineDetails> getAll_cuisine() {
            return all_cuisine;
        }

        public void setAll_cuisine(List<AllCuisineDetails> all_cuisine) {
            this.all_cuisine = all_cuisine;
        }

        public List<AllClientRestDetails> getClients() {
            return clients;
        }

        public void setClients(List<AllClientRestDetails> clients) {
            this.clients = clients;
        }
    }

    public  class  AllClientRestDetails{

        @SerializedName("clientName")
        private String clientName;

        @SerializedName("bgimge")
        private String bgimge;

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getBgimge() {
            return bgimge;
        }

        public void setBgimge(String bgimge) {
            this.bgimge = bgimge;
        }
    }

    public  class  AllCuisineDetails{

        @SerializedName("cuisinesID")
        private String cuisinesID;


        @SerializedName("name")
        private String name;

        @SerializedName("image")
        private String image;

        public String getCuisinesID() {
            return cuisinesID;
        }

        public void setCuisinesID(String cuisinesID) {
            this.cuisinesID = cuisinesID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
