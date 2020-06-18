package com.fbanseptcours.travellerguidmobileandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.fbanseptcours.travellerguidmobileandroid.utils.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserPreferencesController {

    private static UserPreferencesController instance = null;

    private UserPreferencesController() {

    }

    public static UserPreferencesController getInstance() {
        if (instance == null) {
            instance = new UserPreferencesController();
        }
        return instance;
    }

    public interface SuccesConnexionEcouteur {
        void onSuccessConnection();
    }

    public interface ErreurConnexionEcouteur {
        void onErrorConnection(String messageErreur);
    }


    //on crée la méthode qui permet de mettre à jour en bdd les préférences de l'utilisateur, cette méthode sera appelée dans UserPreferencesActivity
    public void updatePreferencesUser(Context context, int id, String budget, Set<String> categories, SuccesConnexionEcouteur ecouteurSucces, ErreurConnexionEcouteur ecouteurErreur) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, RequestManager.url + "users/update",
                response -> {
                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    ecouteurSucces.onSuccessConnection();
                },
                error -> {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    ecouteurErreur.onErrorConnection("Impossible de mettre à jour les préférences");
                }
        ) {


            //on ajoute les headers de notre requete
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", id);
                    jsonBody.put("budget", budget);

                    //pour afficher la liste de catégories en json : afficher un array avec plusieurs objets dedans (plusieurs catégories)
                    JSONArray array = new JSONArray();
                    JSONObject arrayItem;
                    for (String category : categories){
                        Log.d("category",category);
                        arrayItem = new JSONObject();
                        arrayItem.put("type",category);
                        array.put(arrayItem);
                    }
                    jsonBody.put("listCategory",array);

//                    Log.d("json",jsonBody.toString());

                    return jsonBody.toString().getBytes("utf-8");

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        RequestManager.getInstance(context).addToRequestQueue(context, stringRequest);
    }
}
