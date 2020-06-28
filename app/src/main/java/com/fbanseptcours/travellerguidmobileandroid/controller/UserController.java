package com.fbanseptcours.travellerguidmobileandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.fbanseptcours.travellerguidmobileandroid.model.User;
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
    //on définit le nom du fichier "MesPreferences, par convention on le nomme comme cela avec des majuscules
    public final static String FICHIER_PREFERENCE = "MesPreferences";

    private UserController(){

    }

    public static UserController getInstance(){
        if(instance==null){
            instance=new UserController();
        }
        return instance;
    }

    //interface fonctionnelle en cas de succes
    public interface SuccesConnectionListener{
        void onSuccessConnection();
    }

    //interface fonctionnelle en cas d'erreur'
    public interface ErrorConnectionListener{
        void onErrorConnection(String messageErreur);
    }

    //interface créée pour la méthode CreateAccount pour pouvoir récupérer l'id de l'user qui s'inscrit (id est le body de la reponse de la requete authentification)
    public interface SuccessCreateAccountListener{
        void onSuccessConnection(String id);
    }


    //méthode appelée lors de l'authentification d'un user pour récupération du token : appel de la requete authentification de notre api
    public void connexion(Context context, String login, String password, SuccesConnectionListener ecouteurSuccess, ErrorConnectionListener onErrorConnection) {

        //on appelle StringRequest pour récupérer le token
        StringRequest stringRequest = new StringRequest
                //on met ici la requete pour s'authentifier en back avec le type de méthode
                //la variable url est notre adresse pour pouvoir avoir accés aux requetes, elle se trouve dans requestManager
                (Request.Method.POST, RequestManager.url + "authentification",
                        token -> {

//                            Log.d("JWTOKEN", "token non décrypté : " + token);
                            //travail sur le token afin d'isoler le userId ("id" dans le payload)
                            //isolation du payload (tokenSplit[1])

                            String[] tokenSplit = token.split("\\.");
//                            Log.d("JWTOKEN", "token non décrypté, payload isolé : " + tokenSplit[1]);

                            //décodage du payload et "clean-up"
                            Base64.Decoder decoder = Base64.getDecoder();
                            String decryptedPayload = new String(decoder.decode(tokenSplit[1]));
//                            Log.d("JWTOKEN", "payload décrypté : " + decryptedPayload);

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

                            //on peut donc parser l'id de l'utilisateur contenu dans le JSONObject.
                            int userId = 0;
                            try {
                                userId = jsonPayload.getInt("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Log.d("JWTOKEN", "id (en int) : " + userId);

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

            //on ajoute le body dans notre requete : login et password saisis par l'utilisateur pour permettre l'authentification
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

    //méthode qui permet de créer un nouveau compte : appel de la requete inscription de notre api
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

            //ajout du login et du password dans le body pour enregistrement dans la base de données
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

    //méthode qui permet de 'déconnecter' un utilisateur : de supprimer son token du fichier mesPreferences ; il devra se réauthentifier
    public void deconnexion(
            Context context,
            SuccesConnectionListener listenerSuccess
    ){

        SharedPreferences preference = context.getSharedPreferences(FICHIER_PREFERENCE, 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.remove("token");
        editor.apply();
        listenerSuccess.onSuccessConnection();
    }

    //méthode qui permet de vérifier la validité du token présent dans le fichier mesPreferences selon sa date d'expiration
    public boolean isTokenValide(Context context){
        Log.d("token", "je passe par la méthode is tokenValide");
        SharedPreferences preference = context.getSharedPreferences(FICHIER_PREFERENCE, 0);
        String token = preference.getString("token",null);
//        Log.d("token",token);
        if(token != null) {
            try {
                Date expiration = new Date(JWTUtils.getBody(token).getLong("exp") *1000);
                Log.d("token", String.valueOf(expiration));
                    return expiration.after(new Date());
            } catch (UnsupportedEncodingException | JSONException e) {
                return false;
            }
        }
        return false;
    }

    //méthode qui permet de récupérer l'utilisateur qui est connecté par l'intermédiaire de son token et du paramètre sub dans le token qui correspond à son login
    public User getUserConnected(Context context){
        SharedPreferences preference = context.getSharedPreferences(FICHIER_PREFERENCE, 0);
        String token = preference.getString("token",null);

        if(token != null) {

            try {
                JSONObject jsonUtilisateur = JWTUtils.getBody(token);
                User user = new User(
                        jsonUtilisateur.getInt("id"),
                        jsonUtilisateur.getString("sub")
                );

                //si on a besoin de récupérer son rôle
//                String role = jsonUtilisateur.getString("role");

                return user;

            } catch (UnsupportedEncodingException | JSONException e) {
                return new User(0,"-");
            }
        }
        return new User(0,"-");
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