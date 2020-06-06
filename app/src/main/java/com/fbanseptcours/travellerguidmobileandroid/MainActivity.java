package com.fbanseptcours.travellerguidmobileandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fbanseptcours.travellerguidmobileandroid.view.SearchActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch=findViewById(R.id.btn_search);
        btnSearch.setOnClickListener((View v) -> {
            startActivity(new Intent(this, SearchActivity.class));
        });
    }
}
