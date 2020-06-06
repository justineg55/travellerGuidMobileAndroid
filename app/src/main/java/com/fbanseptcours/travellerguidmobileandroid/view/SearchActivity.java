package com.fbanseptcours.travellerguidmobileandroid.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.SpinnerCityController;
import com.fbanseptcours.travellerguidmobileandroid.controller.UserController;
import com.fbanseptcours.travellerguidmobileandroid.model.City;
import com.fbanseptcours.travellerguidmobileandroid.utils.CityAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    private Spinner spinner;
    private Button btnSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        SpinnerCityController.getInstance().getCities(this,(List<City> cities) -> {
            City[] citiestab = new City[cities.size()];
            cities.toArray(citiestab);
            spinner.setAdapter(new CityAdapter(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, citiestab));



        });

        btnSearch=findViewById(R.id.btn_search);
        btnSearch.setOnClickListener((View v) -> {
            startActivity(new Intent(this,ResultsListActivity.class));
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
