package com.fbanseptcours.travellerguidmobileandroid.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.ResultController;
import com.fbanseptcours.travellerguidmobileandroid.model.City;
import com.fbanseptcours.travellerguidmobileandroid.model.Result;
import com.fbanseptcours.travellerguidmobileandroid.utils.CardViewAdapter;

import java.util.List;

public class ResultsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardViewAdapter cardViewAdapter;
    private TextView cityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        cityText= findViewById(R.id.cityText);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = this.getSharedPreferences("MesPreferences", 0);

        //on affiche la ville en haut de la page de résultats dans le textView cityText grâce à l'id city enregistré dans mes préférences lors de la recherche
        ResultController.getInstance().getCity(
                this,sharedPreferences.getInt("cityId", 0),(City cityObject) -> {
                    cityText.setText(cityObject.getCityName());
                }
        );

        //récupération de la liste des résultats dans notre cardViewAdapter
        ResultController.getInstance().getResults(
                this,
                (List<Result> listResults) -> {
                    recyclerView.setAdapter(new CardViewAdapter(listResults));
                }
        );

    }
}
