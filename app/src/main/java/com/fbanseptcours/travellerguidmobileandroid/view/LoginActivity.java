package com.fbanseptcours.travellerguidmobileandroid.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fbanseptcours.travellerguidmobileandroid.MainActivity;
import com.fbanseptcours.travellerguidmobileandroid.R;
import com.fbanseptcours.travellerguidmobileandroid.controller.UserController;

public class LoginActivity extends AppCompatActivity {

    TextView login;
    TextView password;
    Button btnConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=findViewById(R.id.input_login);
        password=findViewById(R.id.input_password);
        btnConnection=findViewById(R.id.btn_connection);

        btnConnection.setOnClickListener( (View v) -> {
            UserController.getInstance().connexion(this, login.getText().toString(),
                    password.getText().toString(),
                    //en cas de succés :
                    () -> startActivity(new Intent(this, MainActivity.class)),
                    // en cas d'erreur :
                    (String messageErreur) -> Toast.makeText(this, messageErreur, Toast.LENGTH_LONG).show()
            );
        });
    }
}
