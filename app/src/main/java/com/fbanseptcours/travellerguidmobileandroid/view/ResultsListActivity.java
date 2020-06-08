package com.fbanseptcours.travellerguidmobileandroid.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.ResultController;
import com.fbanseptcours.travellerguidmobileandroid.model.Result;
import com.fbanseptcours.travellerguidmobileandroid.utils.CardViewAdapter;

import java.util.List;

public class ResultsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardViewAdapter cardViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ResultController.getInstance().getResults(
                this,
                (List<Result> listResults) -> {
                    recyclerView.setAdapter(new CardViewAdapter(listResults));
                }
        );

    }
}
