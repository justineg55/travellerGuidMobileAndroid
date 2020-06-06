package com.fbanseptcours.travellerguidmobileandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.fbanseptcours.travellerguidmobileandroid.utils.RequestManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    private static UserController instance=null;

    private UserController(){

    }

    public static UserController getInstance(){
        if(instance==null){
            instance=new UserController();
        }
        return instance;
    }

    public interface SuccesConnexionEcouteur{
        void onSuccessConnection();
    }

    public interface ErreurConnexionEcouteur{
        void onErrorConnection(String messageErreur);
    }


    public void connexion(Context context, String login, String password, SuccesConnexionEcouteur ecouteurSucces, ErreurConnexionEcouteur ecouteurErreur){
        Log.d("USER_CONTROLEUR", "toto");
        //on appelle StringRequest pour récupérer le token
        StringRequest stringRequest = new StringRequest
                //on met ici la requete pour s'authentifier en back avec le type de méthode
                //la variable url est notre adresse pour pouvoir accés aux requetes, elle se trouve dans requestManager
                (Request.Method.POST, RequestManager.url + "authentification",
                        token -> {
                            //on stocke le token dans un objet préférences
                            //fichier mesprefeences stocké dans fichier de l'appli
                            SharedPreferences preference = context.getSharedPreferences("MesPreferences",0); //0 pour private mode
                            SharedPreferences.Editor editor = preference.edit();
                            //on associe la clé/valeur
                            editor.putString("token", token); // Storing string
                            //on sauvegarde le fichier texte
                            editor.apply();

                            ecouteurSucces.onSuccessConnection();

                        },
                        //en cas d'erreur c'est un toast qui s'active
                        error -> ecouteurErreur.onErrorConnection("Impossible de se connecter")
                ){

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
                    jsonBody.put("login", login);
                    jsonBody.put("password", password);

                    return jsonBody.toString().getBytes("utf-8");

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        RequestManager.getInstance(context).addToRequestQueue(context,stringRequest);

    }



}
