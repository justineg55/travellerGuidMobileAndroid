package com.fbanseptcours.travellerguidmobileandroid.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;
    private String login;


    public User (Integer id, String login){
        this.id = id;
        this.login=login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


}
