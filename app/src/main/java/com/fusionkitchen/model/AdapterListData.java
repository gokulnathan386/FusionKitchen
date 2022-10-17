package com.fusionkitchen.model;

public class AdapterListData {
    public String today_time_string;
    public String today_time;
    public String label;

    public AdapterListData(String today_time_string, String today_time,String label){
        this.today_time_string = today_time_string;
        this.today_time = today_time;
        this.label = label;
    }
    @Override
    public String toString(){
        return today_time;
    }

}