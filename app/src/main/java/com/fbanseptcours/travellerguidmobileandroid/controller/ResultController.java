package com.fbanseptcours.travellerguidmobileandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fbanseptcours.travellerguidmobileandroid.model.City;
import com.fbanseptcours.travellerguidmobileandroid.model.Result;
import com.fbanseptcours.travellerguidmobileandroid.model.User;
import com.fbanseptcours.travellerguidmobileandroid.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;
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
                Log.d("token",preference.getString("token",""));
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + preference.getString("token", ""));
                return params;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();

                    Log.d("debug","getbody");

                    List<String> periodList=new ArrayList<>();
                    periodList.add("matin");
                    periodList.add("midi");
                    jsonBody.put("cityId",1);
                    jsonBody.put( "period",new JSONArray(periodList));
                    jsonBody.put("userId", 3);



//                    JSONArray jsonArray = new JSONArray();
//                    for (String s : periodList)
//                        jsonArray.put(s);
//
//                    jsonBody.put("period", jsonArray);

                    return jsonBody.toString().getBytes("utf-8");

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        RequestManager.getInstance(context).addToRequestQueue(context, jsonArrayRequest);

    }
}
