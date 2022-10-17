package com.fusionkitchen.model;

public class The_Slide_Items_Model_Class {

    private int featured_image;
    private String the_caption_Title;
    private String sub_title;

    public The_Slide_Items_Model_Class(int hero, String title,String sub_title) {
        this.featured_image = hero;
        this.the_caption_Title = title;
        this.sub_title = sub_title;
    }

    public int getFeatured_image() {
        return featured_image;
    }

    public String getThe_caption_Title() {
        return the_caption_Title;
    }


    public void setFeatured_image(int featured_image) {
        this.featured_image = featured_image;
    }

    public void setThe_caption_Title(String the_caption_Title) {
        this.the_caption_Title = the_caption_Title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getSub_title() {
        return sub_title;
    }

}
