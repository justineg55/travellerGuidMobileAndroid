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
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    private static UserController instance=null;

    private static final String DEBUG_TAG = "USER_CONTROLLER";

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


    public void connexion(Context context, String login, String password, SuccesConnexionEcouteur ecouteurSucces, ErreurConnexionEcouteur ecouteurErreur) {
        Log.d("USER_CONTROLEUR", "toto");
        //on appelle StringRequest pour récupérer le token
        StringRequest stringRequest = new StringRequest
                //on met ici la requete pour s'authentifier en back avec le type de méthode
                //la variable url est notre adresse pour pouvoir accés aux requetes, elle se trouve dans requestManager
                (Request.Method.POST, RequestManager.url + "authentification",
                        token -> {

                            Log.d("JWTOKEN", "token non décrypté : " + token);
                            //travail sur le token afin d'isoler le userId ("id" dans le payload)
                            //isolation du payload (tokenSplit[1])

                            String[] tokenSplit = token.split("\\.");
                            Log.d("JWTOKEN", "token non décrypté, payload isolé : " + tokenSplit[1]);

                            //décodage du payload et "clean-up"
                            Base64.Decoder decoder = Base64.getDecoder();
                            String decryptedPayload = new String(decoder.decode(tokenSplit[1]));
                            Log.d("JWTOKEN", "payload décrypté : " + decryptedPayload);

                            //on va créer un JSONObject à partir du payload
                            //pour ça on va vérifier que notre payload fera un bon JSON avec le JSONTokener
                            JSONTokener jsonTokener = new JSONTokener(decryptedPayload);

                            JSONObject jsonPayload = new JSONObject();
                            try {
                                //JSONTokener va nous permettre de construire le JSONObject
                                jsonPayload = new JSONObject(jsonTokener);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //on peut donc, parser en toute tranquillité l'id de l'utilisateur contenu dans le JSONObject.
                            int userId = 0;
                            try {
                                userId = jsonPayload.getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("JWTOKEN", "id (en int) : " + userId);

                            //on stocke le token et le userId dans un objet préférences
                            //fichier mesprefeences stocké dans fichier de l'appli
                            SharedPreferences preference = context.getSharedPreferences("MesPreferences", 0); //0 pour private mode
                            SharedPreferences.Editor editor = preference.edit();

                            //on associe la clé/valeur
                            editor.putString("token", token); // Storing string
                            editor.putInt("userId", userId); // Storing int

                            //on sauvegarde le fichier texte
                            editor.apply();

                            ecouteurSucces.onSuccessConnection();

                        },
                        //en cas d'erreur c'est un toast qui s'active
                        error -> ecouteurErreur.onErrorConnection("Impossible de se connecter")
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



// méthode connexion v2 avec un un split et un replace et un hashMap
//public void connexion(Context context, String login, String password, SuccesConnexionEcouteur ecouteurSucces, ErreurConnexionEcouteur ecouteurErreur){
//        //on appelle StringRequest pour récupérer le token
//        StringRequest stringRequest = new StringRequest
//        //on met ici la requete pour s'authentifier en back avec le type de méthode
//        //la variable url est notre adresse pour pouvoir accés aux requetes, elle se trouve dans requestManager
//        (Request.Method.POST, RequestManager.url + "authentification",
//        token -> {
//        //travail sur le token afin d'isoler le userId ("id" dans le payload)
//        //isolation du payload (tokenSplit[1])
//
//        String[] tokenSplit = token.split("\\.");
////                            Log.d("JWTOKEN", tokenSplit[1]);
//
//        Base64.Decoder decoder = Base64.getDecoder();
//        String decryptedPayload = new String(decoder.decode(tokenSplit[1]))
//        .replace("{","")
//        .replace("\"","")
//        .replace("}","");
//        Log.d("JWTOKEN", "STRING: " + decryptedPayload);
//
//        //on isole chaque élément du payload dans un tableau (split par la virgule)
//        String[] decryptedPayload_Split = decryptedPayload.split(",");
//        Log.d("JWTOKEN", "STRING: " + Arrays.toString(decryptedPayload_Split));
//
//        //on entre les données dans un HashMap de <String,String>
//        //(pas obligatoire)
//        HashMap<String, String> token_hashMap = new HashMap<>();
//        for (String s : decryptedPayload_Split)
//        token_hashMap.put(s.split(":")[0], s.split(":")[1]);
//
//        //on peut vérifier que dans le HashMap, à la clé "id" il y a bien l'id de notre utilisateur
//        Log.d("JWTOKEN", "id : " + token_hashMap.get("id"));
//
//        //on peut donc, parser en toute tranquillité l'id.
//        int userId = Integer.parseInt(token_hashMap.get("id"));
//        Log.d("JWTOKEN", "id (en int) : " + userId);
//
//
//        //on stocke le token et le userId dans un objet préférences
//        //fichier mesprefeences stocké dans fichier de l'appli
//        SharedPreferences preference = context.getSharedPreferences("MesPreferences", 0); //0 pour private mode
//        SharedPreferences.Editor editor = preference.edit();
//
//        //on associe la clé/valeur
//        editor.putString("token", token); // Storing string
//        editor.putInt("userId", userId); // Storing int
//
//        //on sauvegarde le fichier texte
//        editor.apply();
//
//        ecouteurSucces.onSuccessConnection();
//
//        },
//        //en cas d'erreur c'est un toast qui s'active
//        error -> ecouteurErreur.onErrorConnection("Impossible de se connecter")
//        ){


// méthode connexion avec un split, substring pour récupérer l'id utilisateur ! attention ne focntionne pas avec plusieurs chiffres dans l'id
//    public void connexion(Context context, String login, String password, SuccesConnexionEcouteur ecouteurSucces, ErreurConnexionEcouteur ecouteurErreur){
//        //on appelle StringRequest pour récupérer le token
//        StringRequest stringRequest = new StringRequest
//                //on met ici la requete pour s'authentifier en back avec le type de méthode
//                //la variable url est notre adresse pour pouvoir accés aux requetes, elle se trouve dans requestManager
//                (Request.Method.POST, RequestManager.url + "authentification",
//                        token -> {
//                            //travail sur le token afin d'isoler le userId ("id" dans le payload)
//                            //isolation du payload (tokenSplit[1])
//
//                            String[] tokenSplit = token.split("\\.");
////                            Log.d("JWTOKEN", tokenSplit[1]);
//
////
//////                          décodage du payload
////                            Base64.Decoder decoder = Base64.getDecoder();
////                            String decryptedPayload = new String(decoder.decode(tokenSplit[1]));
//////                            Log.d("JWTOKEN", "STRING: " + decryptedPayload);
////
//////                          on isole le userId (en String)
////                            String userId_String = decryptedPayload.substring(
////                                    decryptedPayload.indexOf("id") + 4,
////                                    decryptedPayload.indexOf("id") + 5
////                            );
////
////                            //parse en int du userId_String
////                            int userId = Integer.parseInt(userId_String);
////                            Log.d(DEBUG_TAG, "userId after parsing : " + userId);
//
//                            //on stocke le token et le userId dans un objet préférences
//                            //fichier mesprefeences stocké dans fichier de l'appli
//                            SharedPreferences preference = context.getSharedPreferences("MesPreferences", 0); //0 pour private mode
//                            SharedPreferences.Editor editor = preference.edit();
//
//                            //on associe la clé/valeur
//                            editor.putString("token", token); // Storing string
//                            editor.putInt("userId", userId); // Storing int
//
//                            //on sauvegarde le fichier texte
//                            editor.apply();
//
//                            ecouteurSucces.onSuccessConnection();
//
//                        },
//                        //en cas d'erreur c'est un toast qui s'active
//                        error -> ecouteurErreur.onErrorConnection("Impossible de se connecter")
//                ){