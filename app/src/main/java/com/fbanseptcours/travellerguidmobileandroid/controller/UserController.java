package com.fbanseptcours.travellerguidmobileandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.fbanseptcours.travellerguidmobileandroid.utils.JWTUtils;
import com.fbanseptcours.travellerguidmobileandroid.utils.RequestManager;
import com.fbanseptcours.travellerguidmobileandroid.view.MenuFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    private static UserController instance=null;
    public final static String FICHIER_PREFERENCE = "MesPreferences";

    private UserController(){

    }

    public static UserController getInstance(){
        if(instance==null){
            instance=new UserController();
        }
        return instance;
    }

    public interface SuccesConnectionListener{
        void onSuccessConnection();
    }

    public interface ErrorConnectionListener{
        void onErrorConnection(String messageErreur);
    }

    //interface créée pour la méthode CreateAccount pour pouvoir récupérer l'id de l'user qui s'enregistre (id est le body de la reponse de la requete authentification)
    public interface SuccessCreateAccountListener{
        void onSuccessConnection(String id);
    }


    public void connexion(Context context, String login, String password, SuccesConnectionListener ecouteurSuccess, ErrorConnectionListener onErrorConnection) {
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
                            SharedPreferences preference = context.getSharedPreferences(FICHIER_PREFERENCE, 0); //0 pour private mode
                            SharedPreferences.Editor editor = preference.edit();

                            //on associe la clé/valeur
                            editor.putString("token", token); // Storing string
                            editor.putInt("userId", userId); // Storing int

                            //on sauvegarde le fichier texte
                            editor.apply();

                            ecouteurSuccess.onSuccessConnection();

                        },
                        //en cas d'erreur c'est un toast qui s'active
                        error -> onErrorConnection.onErrorConnection("Impossible de se connecter")
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

    //méthode qui permet de créer un nouveau compte
    public void createAccount(Context context, String username, String password, SuccessCreateAccountListener ecouteurSucces, ErrorConnectionListener ecouteurErreur) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, RequestManager.url + "inscription",
                response -> {

                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    ecouteurSucces.onSuccessConnection(response);
                },
                error -> {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    ecouteurErreur.onErrorConnection("Impossible de s'enregistrer");
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }


            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("login", username);
                    jsonBody.put("password", password);

                    return jsonBody.toString().getBytes(StandardCharsets.UTF_8);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        RequestManager.getInstance(context).addToRequestQueue(context, stringRequest);

    }


    public void deconnexion(
            Context context,
            SuccesConnectionListener listenerSuccess
    ){
        Log.d("deco","je rentre dans la methode");
        SharedPreferences preference = context.getSharedPreferences(FICHIER_PREFERENCE, 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.remove("token");
        editor.apply();
        listenerSuccess.onSuccessConnection();
    }


    public boolean isTokenValide(Context context){
        SharedPreferences preference = context.getSharedPreferences(FICHIER_PREFERENCE, 0);
        String token = preference.getString("token",null);
        if(token != null) {
            try {
                Date expiration = new Date(JWTUtils.getBody(token).getLong("exp"));
                Log.d("date exp", String.valueOf(expiration));
                if(expiration.before(new Date())){
                    return true;
                }
            } catch (UnsupportedEncodingException | JSONException e) {
                return false;
            }
        }
        return false;
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