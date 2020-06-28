package com.fbanseptcours.travellerguidmobileandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fbanseptcours.travellerguidmobileandroid.model.City;
import com.fbanseptcours.travellerguidmobileandroid.model.Result;
import com.fbanseptcours.travellerguidmobileandroid.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultController {

    private static ResultController instance=null;

    private ResultController(){

    }

    public static ResultController getInstance(){
        if(instance==null){
            instance=new ResultController();
        }
        return instance;
    }

    public interface DownloadResultsListListener {
        void onListResultsDownloaded(List<Result> results);
    }

    //appel de l'api spring sur la requete search pour récupérer les résultats de la recherche : récupération de l'activité et du budget
    public void getResults(Context context, ResultController.DownloadResultsListListener event) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest

                (Request.Method.POST, RequestManager.url + "search", null,
                        response -> {

                            try {
                                List<Result> results= new ArrayList<>();

                                for(int i = 0; i < response.length(); i++) {
                                    results.add(new Result(
                                            response.getJSONObject(i).getInt("id"),
                                            response.getJSONObject(i).getString("name"),
                                            response.getJSONObject(i).getString("budget")));
                                }

                                event.onListResultsDownloaded(results);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Log.d("Erreur", error.toString())
                ) {


            @Override
            public Map<String, String> getHeaders() {

                SharedPreferences preference = context.getSharedPreferences("MesPreferences", 0); // 0 - for private mode
//                Log.d("token",preference.getString("token",""));
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + preference.getString("token", ""));
                return params;
            }

            //méthode qui permet d'ajouter un body à notre requête avec les préférences de l'utilisateur (ses catégories et son budget) ainsi que la ville et les périodes choisies
            @Override
            public byte[] getBody() {
                try {
                        JSONObject jsonBody = new JSONObject();

//
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MesPreferences", 0);
//                        //on récupère l'idcity enregistrée lors de la recherche de l'utilisateur
                        jsonBody.put("cityId", sharedPreferences.getInt("cityId", 0));

                        List<String> periodList = new ArrayList<>(sharedPreferences.getStringSet("period", null));

//                        //on récupère la liste des périodes sélectinnées par l'utilisateur lors de la recherche
                        jsonBody.put("period", new JSONArray(periodList));
//                        //on récupère l'id de l'utilisateur enregistré dans MesPreferences lors qu'il s'est logué
                        jsonBody.put("userId", sharedPreferences.getInt("userId",0));

                        return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

        };

        RequestManager.getInstance(context).addToRequestQueue(context, jsonArrayRequest);

    }


    public interface DownloadCityListener {
        void onCityDownloaded(City city);
    }


    //récupération de la ville choisie par l'utilisateur grâce à l'appel à la requête getCitybyid de l'api pour affichage dans pa page de résultats
    public void getCity(Context context, int idCity, DownloadCityListener event
    ) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, RequestManager.url+ "cities/" + idCity, null,
                            response -> {
                                try {
                                    City city = new City(
                                            response.getInt("id"),
                                            response.getString("cityName")
                                            );

                                    event.onCityDownloaded(city);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> Log.d("Erreur", error.toString()))
            {
                @Override
                public Map<String, String> getHeaders() {

                    SharedPreferences preference = context.getSharedPreferences("MesPreferences", 0); // 0 - for private mode
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + preference.getString("token", ""));
                    return params;
                }
            };

            RequestManager.getInstance(context).addToRequestQueue(context, jsonObjectRequest);
        }
    }

