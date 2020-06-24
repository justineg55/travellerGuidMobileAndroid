package com.fbanseptcours.travellerguidmobileandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fbanseptcours.travellerguidmobileandroid.MainActivity;
import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.UserController;
import com.fbanseptcours.travellerguidmobileandroid.controller.UserPreferencesController;
import com.fbanseptcours.travellerguidmobileandroid.model.User;

import java.util.HashSet;
import java.util.Set;

public class SettingsUserPreferencesActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_settings_userpreferences);

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

        //récupération de l'id du user connecté grâce à la méthode getUserConnected
        User userConnected = UserController.getInstance().getUserConnected(this);
        int idUserConnected=userConnected.getId();

        //au clic sur le bouton valider ses préférences, on enregistre ses préférences et on le renvoie sur la page main activity
        btnValider.setOnClickListener( (View v) ->
                UserPreferencesController.getInstance().updatePreferencesUser(
                        this,idUserConnected,getBudget(),getCategories(),
                        () -> startActivity(new Intent(this, MainActivity.class)),
                        (String messageErreur) -> Toast.makeText(this, messageErreur, Toast.LENGTH_LONG).show()));

        }

        //méthode pour récupérer le budget sélectionné par l'utilisateur
    private String getBudget() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButtonSelected = findViewById(selectedId);
        return radioButtonSelected.getText().toString();
    }

    //méthode pour récupérer les catégories sélectionnées par l'utilisateur
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

