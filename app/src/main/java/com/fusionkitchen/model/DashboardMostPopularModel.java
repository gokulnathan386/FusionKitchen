package com.fusionkitchen.model;

public class DashboardMostPopularModel {
    private String name;
    private String area;
    private String rating_average;
    private String takeawaystatus;
    private String discount;
    private String image_url;
    private String postcode;
    private String address_location;
    private String menupageurl;
    private String lat;
    private String lang;


    public DashboardMostPopularModel(String name, String area, String rating_average,
                                     String takeawaystatus,String discount,String image_url,
                                     String postcode,String address_location,String menupageurl,String lat,String lang) {
        this.name = name;
        this.area = area;
        this.rating_average=rating_average;
        this.takeawaystatus=takeawaystatus;
        this.discount = discount;
        this.image_url =image_url;
        this.postcode = postcode;
        this.address_location = address_location;
        this.menupageurl = menupageurl;
        this.lat = lat;
        this.lang = lang;
    }

    public String getDiscount() {
        return discount;
    }

    public String getArea() {
        return area;

    }

    public String getRating_average() {
        return rating_average;
    }

    public String getTakeawaystatus() {
        return takeawaystatus;
    }

    public String getName() {
        return name;
    }

    public  String  getImage_url(){
        return image_url;
    }

    public  String  getPost_code(){
        return postcode;
    }

    public  String  getaddress_Location(){
        return address_location;
    }


    public  String  getmenupage_url(){
        return menupageurl;
    }

    public  String  getLat(){
        return lat;
    }

    public  String  getLang(){
        return lang;
    }
}
