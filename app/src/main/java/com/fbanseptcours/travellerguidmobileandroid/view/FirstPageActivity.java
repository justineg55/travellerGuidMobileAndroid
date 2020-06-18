package com.fbanseptcours.travellerguidmobileandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fbanseptcours.travellerguidmobileandroid.R;

public class FirstPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        final Button btn_register = findViewById(R.id.btn_create_account);
        final Button btn_login = findViewById(R.id.btn_login);
        final TextView txt_welcome = findViewById(R.id.txt_welcome);


        txt_welcome.setText(String.format("%s%s%s",
                "Bienvenue sur l'app du routard !",
                "\n Si vous n'avez pas de compte, veuillez en créer un.",
                "\n Sinon, vous pouvez vous authentifier"));

        btn_register.setOnClickListener((View v) -> startActivity(new Intent(this, RegisterActivity.class)));
        btn_login.setOnClickListener((View v) -> startActivity(new Intent(this, LoginActivity.class)));
    }
}
