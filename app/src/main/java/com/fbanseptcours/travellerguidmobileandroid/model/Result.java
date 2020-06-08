package com.fbanseptcours.travellerguidmobileandroid.model;

import java.io.Serializable;

public class Result implements Serializable {
    private Integer id;
    private String activityName;
//    String period;
    private String budget;
//    String category;

    public Result(Integer id,String activityName, String budget) {
        this.id = id;
        this.activityName = activityName;
//        this.period = period;
        this.budget = budget;
//        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivity() {
        return activityName;
    }

    public void setActivity(String activity) {
        this.activityName = activityName;
    }

//    public String getPeriod() {
//        return period;
//    }
//
//    public void setPeriod(String period) {
//        this.period = period;
//    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
}
