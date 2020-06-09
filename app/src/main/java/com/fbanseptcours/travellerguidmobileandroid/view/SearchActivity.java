
package com.fbanseptcours.travellerguidmobileandroid.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.SpinnerCityController;
import com.fbanseptcours.travellerguidmobileandroid.model.City;
import com.fbanseptcours.travellerguidmobileandroid.utils.CityAdapter;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    private Spinner spinner;
    private Button btnSearch;
    private CheckBox cBox_matin;
    private CheckBox cBox_midi;
    private CheckBox cBox_aprem;
    private CheckBox cBox_soir;
    private CheckBox cBox_nuit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Set<String> periods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        cBox_matin = findViewById(R.id.cBox_matin);
        cBox_midi = findViewById(R.id.cBox_midi);
        cBox_aprem = findViewById(R.id.cBox_aprem);
        cBox_soir = findViewById(R.id.cBox_soir);
        cBox_nuit = findViewById(R.id.cBox_nuit);

        periods = new HashSet<>();

        SpinnerCityController.getInstance().getCities(this,(List<City> cities) -> {
            City[] citiestab = new City[cities.size()];
            cities.toArray(citiestab);
            spinner.setAdapter(new CityAdapter(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, citiestab));
        });

        //au clic du bouton rechercher on enregistre les choix de l'utilisateur dans le fichier MesPreferences : la city et les périodes sélectionnées
        btnSearch=findViewById(R.id.btn_search);
        btnSearch.setOnClickListener((View v) -> {
            sharedPreferences = getSharedPreferences("MesPreferences", 0);
            editor = sharedPreferences.edit();
            editor.putInt("cityId", (int) spinner.getSelectedItemId() + 1);
            editor.putStringSet("period", getPeriods());
            editor.apply();

            startActivity(new Intent(this,ResultsListActivity.class));
        });
    }

    //méthode permettant de récupérer la liste des périodes sélectionnées par l'utilisateur
    private Set<String> getPeriods() {
        if ( !cBox_matin.isChecked() && !cBox_midi.isChecked() && !cBox_aprem.isChecked() && !cBox_soir.isChecked() && !cBox_nuit.isChecked() )
            periods.add("matin");

        if (cBox_matin.isChecked())
            periods.add("matin");

        if (cBox_midi.isChecked())
            periods.add("midi");

        if (cBox_aprem.isChecked())
            periods.add("après-midi");

        if (cBox_soir.isChecked())
            periods.add("soir");

        if (cBox_nuit.isChecked())
            periods.add("nuit");

        return periods;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

//package com.fbanseptcours.travellerguidmobileandroid.view;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//
//import com.fbanseptcours.travellerguidmobileandroid.R;
//import com.fbanseptcours.travellerguidmobileandroid.controller.SpinnerCityController;
//import com.fbanseptcours.travellerguidmobileandroid.controller.UserController;
//import com.fbanseptcours.travellerguidmobileandroid.model.City;
//import com.fbanseptcours.travellerguidmobileandroid.utils.CityAdapter;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class SearchActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{
//
//    private Spinner spinner;
//    private Button btnSearch;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//
//        spinner = findViewById(R.id.spinner);
//
//        spinner.setOnItemSelectedListener(this);
//
//        SpinnerCityController.getInstance().getCities(this,(List<City> cities) -> {
//            City[] citiestab = new City[cities.size()];
//            cities.toArray(citiestab);
//            spinner.setAdapter(new CityAdapter(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, citiestab));
//
//
//
//        });
//
//        btnSearch=findViewById(R.id.btn_search);
//        btnSearch.setOnClickListener((View v) -> {
//            startActivity(new Intent(this,ResultsListActivity.class));
//        });
//    }