package com.fbanseptcours.travellerguidmobileandroid.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class RequestManager {

    //on ajoute notre début d'url correspondant à notre api pour nos requetes
    public static final String url="http://192.168.1.28:8080/";

    private static RequestManager instance;
    private RequestQueue requestQueue;


    private RequestManager(Context context) {
        requestQueue = getRequestQueue(context);
    }
    public static synchronized RequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public void addToRequestQueue(Context context,Request req) {
        getRequestQueue(context).add(req);
    }
}
