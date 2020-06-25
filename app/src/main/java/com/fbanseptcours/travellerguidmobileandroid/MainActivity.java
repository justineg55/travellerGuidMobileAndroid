package com.fbanseptcours.travellerguidmobileandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fbanseptcours.travellerguidmobileandroid.controller.UserController;
import com.fbanseptcours.travellerguidmobileandroid.model.User;
import com.fbanseptcours.travellerguidmobileandroid.view.SearchActivity;

public class MainActivity extends AppCompatActivity {

//    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtLogin=findViewById(R.id.txt_loginUser);

        //affichage du login de l'utilsiateur connecté
        User userConnected = UserController.getInstance().getUserConnected(this);
        txtLogin.setText(userConnected.getLogin());
        
    }
}
