package com.fusionkitchen.model;
 import java.util.ArrayList;

public class ExamData {
    ArrayList<String> name;
    String date;


    public ExamData(ArrayList<String> name,
                    String date
    )
    {
        this.name = name;
        this.date = date;

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}