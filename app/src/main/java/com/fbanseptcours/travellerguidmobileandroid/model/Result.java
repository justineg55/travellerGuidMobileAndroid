package com.fbanseptcours.travellerguidmobileandroid.model;

import java.io.Serializable;

public class Result implements Serializable {
    private Integer id;
    private String activityName;
    private String budget;

    public Result(Integer id,String activityName, String budget) {
        this.id = id;
        this.activityName = activityName;
        this.budget = budget;
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


    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

}
