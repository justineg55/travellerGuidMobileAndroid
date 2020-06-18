package com.fbanseptcours.travellerguidmobileandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.UserController;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView txt_infos = findViewById(R.id.txt_infos);
        txt_infos.setText("Veuillez entrer votre login et votre mot de passe");

        final EditText input_username = findViewById(R.id.input_username);
        final EditText input_password = findViewById(R.id.input_password);
        final Button register_account = findViewById(R.id.btn_create_account);

        register_account.setOnClickListener( (View v) -> UserController.getInstance().createAccount(
                this,
                input_username.getText().toString(),
                input_password.getText().toString(),
                () -> startActivity(new Intent(this, UserPreferencesActivity.class)),
                (error) -> Toast.makeText(this, error, Toast.LENGTH_LONG).show()));
    }
}
