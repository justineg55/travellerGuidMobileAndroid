package com.fbanseptcours.travellerguidmobileandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fbanseptcours.travellerguidmobileandroid.model.City;
import com.fbanseptcours.travellerguidmobileandroid.utils.RequestManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpinnerCityController {

    private static SpinnerCityController instance=null;

    private SpinnerCityController(){

    }

    public static SpinnerCityController getInstance(){
        if(instance==null){
            instance=new SpinnerCityController();
        }
        return instance;
    }

    //on va récupérer la liste de cities du back
    public interface DownloadCitiesListListener {
        void onListCitiesDownloaded(List<City> cities);
    }

    public void getCities(Context context, DownloadCitiesListListener event) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest

                (Request.Method.GET, RequestManager.url + "cities", null,
                        response -> {

                            try {
                                List<City> cities= new ArrayList<>();

                                for(int i = 0; i < response.length(); i++) {
                                    cities.add(new City(
                                            response.getJSONObject(i).getInt("id"),
                                            response.getJSONObject(i).getString("cityName")));
                                }

                                event.onListCitiesDownloaded(cities);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Log.d("Erreur", error.toString())
                ) {
            //ajout token dans la méthode
            @Override
            public Map<String, String> getHeaders() {

                SharedPreferences preference = context.getSharedPreferences("MesPreferences", 0); // 0 - for private mode
                Log.d("token",preference.getString("token",""));
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + preference.getString("token", ""));
                return params;
            }


        };
        RequestManager.getInstance(context).addToRequestQueue(context, jsonArrayRequest);

    }
}
