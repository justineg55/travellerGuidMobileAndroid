package com.fbanseptcours.travellerguidmobileandroid.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fbanseptcours.travellerguidmobileandroid.MainActivity;
import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.UserPreferencesController;

import java.util.HashSet;
import java.util.Set;

public class UserPreferencesActivity extends AppCompatActivity {

    private CheckBox cBox1;
    private CheckBox cBox2;
    private CheckBox cBox3;
    private CheckBox cBox4;
    private CheckBox cBox5;
    private CheckBox cBox6;

    private RadioGroup radioGroup;
    private RadioButton budgetMin;
    private RadioButton budgetMoy;
    private RadioButton budgetMax;

    private RadioButton radioButtonSelected;

    private Button btnValider;

    Set<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);

        cBox1 = findViewById(R.id.checkBox);
        cBox2= findViewById(R.id.checkBox2);
        cBox3 = findViewById(R.id.checkBox3);
        cBox4 = findViewById(R.id.checkBox4);
        cBox5 = findViewById(R.id.checkBox5);
        cBox6 = findViewById(R.id.checkBox6);

        radioGroup=findViewById(R.id.radiogrp);

        budgetMin = findViewById(R.id.radio_min);
        budgetMoy = findViewById(R.id.radio_moyen);
        budgetMax = findViewById(R.id.radio_max);

        btnValider = findViewById(R.id.btn_valider);

        categories=new HashSet<>();

        //ici on récupère la donnée id (qui est l'id de l'utilisateur nouvellement inscrit) envoyée dans cette activité par l'activité RegisterActivity au clic du bouton s'enregistrer grâce à la méthode .putextra
        Intent i1 = getIntent();
        int idResponse= Integer.parseInt(i1.getStringExtra("id"));

        //au clic sur le bouton valider, on enregistre les préférences (liste de categories et son budget) dans la bdd et on envoie l'utilisateur sur la page d'accueil
        //attention il faut que l'utilisateur s'authentifie pour qu'on puisse lui générer son token ! ! !! ! !

//        btnValider.setOnClickListener( (View v) ->
//            UserPreferencesController.getInstance().updatePreferencesUser(
//                    this,idResponse,getBudget(),getCategories(),
//                    () -> startActivity(new Intent(this, MainActivity.class)),
//                            (String messageErreur) -> Toast.makeText(this, messageErreur, Toast.LENGTH_LONG).show()));

        //au clic sur le bouton valider, on enregistre ses préférences et on l'envoie sur la page de login pour qu'il puisse s'authentifier et avoir son token
        btnValider.setOnClickListener( (View v) ->
                UserPreferencesController.getInstance().updatePreferencesUser(
                        this,idResponse,getBudget(),getCategories(),
                        () -> startActivity(new Intent(this, LoginActivity.class)),
                        (String messageErreur) -> Toast.makeText(this, messageErreur, Toast.LENGTH_LONG).show()));

        }

        //méthode pour récupérer le budget sélectionné par l'utilisateur
    private String getBudget() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButtonSelected = findViewById(selectedId);
        return radioButtonSelected.getText().toString();
    }

    //méthode pour récupérer lles catégories sélectionnées par l'utilisateur
    //s'il ne sélectionne rien, par défaut il n'y aura pas de filtre effectué avec la catégorie
        private Set<String> getCategories() {
            if ( !cBox1.isChecked() && !cBox2.isChecked() && !cBox3.isChecked() && !cBox4.isChecked() && !cBox5.isChecked() && !cBox6.isChecked()) {
                categories.add(cBox1.getText().toString());
                categories.add(cBox2.getText().toString());
                categories.add(cBox3.getText().toString());
                categories.add(cBox4.getText().toString());
                categories.add(cBox5.getText().toString());
                categories.add(cBox6.getText().toString());
            }
            if (cBox1.isChecked())
                categories.add(cBox1.getText().toString());

            if (cBox2.isChecked())
                categories.add(cBox2.getText().toString());

            if (cBox3.isChecked())
                categories.add(cBox3.getText().toString());

            if (cBox4.isChecked())
                categories.add(cBox4.getText().toString());

            if (cBox5.isChecked())
                categories.add(cBox5.getText().toString());

            if (cBox6.isChecked())
                categories.add(cBox6.getText().toString());

            return categories;
        }

}

