package com.fusionkitchen.model.moreinfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class about_us_model {

    //AccountResultModel
    @SerializedName("STATUS")
    private String STATUS;




    @SerializedName("about")
    private about_us_model.aboutdetails about;

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }





    public aboutdetails getAbout() {
        return about;
    }

    public void setAbout(aboutdetails about) {
        this.about = about;
    }

    public class aboutdetails {

        @SerializedName("aboutus")
        private String aboutus;


        @SerializedName("about_email")
        private String about_email;



        @SerializedName("openinghours")
        private List<about_us_model.aboutdetails.openinghours> openinghours;


        @SerializedName("googlemaps")
        private about_us_model.aboutdetails.googlemaps googlemaps;


        @SerializedName("cuisines")
        private List<about_us_model.aboutdetails.cuisines> cuisines;


        public String getAboutus() {
            return aboutus;
        }

        public void setAboutus(String aboutus) {
            this.aboutus = aboutus;
        }



        public String getAboutEmail() {
            return about_email;
        }

        public void setAboutEmail(String about_email) {
            this.about_email = about_email;
        }





        public aboutdetails.googlemaps getGooglemaps() {
            return googlemaps;
        }

        public void setGooglemaps(aboutdetails.googlemaps googlemaps) {
            this.googlemaps = googlemaps;
        }

        public List<aboutdetails.cuisines> getCuisines() {
            return cuisines;
        }

        public void setCuisines(List<aboutdetails.cuisines> cuisines) {
            this.cuisines = cuisines;
        }


        public List<aboutdetails.openinghours> getOpeninghours() {
            return openinghours;
        }

        public void setOpeninghours(List<aboutdetails.openinghours> openinghours) {
            this.openinghours = openinghours;
        }


        public class openinghours{

            @SerializedName("day")
            private String day;
            @SerializedName("time")
            private String time;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
        public class googlemaps{
            @SerializedName("name")
            private String name;
            @SerializedName("address1")
            private String address1;
            @SerializedName("address2")
            private String address2;
            @SerializedName("city")
            private String city;
            @SerializedName("state")
            private String state;
            @SerializedName("pincode")
            private String pincode;

            @SerializedName("bg")
            private String bg;


            public String getBg() {
                return bg;
            }

            public void setBg(String bg) {
                this.bg = bg;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress1() {
                return address1;
            }

            public void setAddress1(String address1) {
                this.address1 = address1;
            }

            public String getAddress2() {
                return address2;
            }

            public void setAddress2(String address2) {
                this.address2 = address2;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getPincode() {
                return pincode;
            }

            public void setPincode(String pincode) {
                this.pincode = pincode;
            }
        }

        public class cuisines{
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
