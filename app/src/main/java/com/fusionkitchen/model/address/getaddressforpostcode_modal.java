package com.fusionkitchen.model.address;

import com.google.gson.annotations.SerializedName;

public class getaddressforpostcode_modal {


    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("address")
    private getaddressforpostcode_modal.address address;

    @SerializedName("error_message")
    private String error_message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String geterror_message() {
        return error_message;
    }

    public void seterror_message(String error_message) {
        this.error_message = error_message;
    }


    public getaddressforpostcode_modal.address getAddress() {
        return address;
    }

    public void setAddress(getaddressforpostcode_modal.address address) {
        this.address = address;
    }

    public class address{

        @SerializedName("street")
        private String street;

        @SerializedName("town")
        private String town;

        @SerializedName("postcode")
        private String postcode;


        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }
}
