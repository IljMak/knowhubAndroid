package com.example.knowhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowhub.data.UserDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private UserDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisierung der UI-Elemente
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // Initialisierung des DatabaseHelpers
        databaseHelper = new UserDatabaseHelper(this);

        // Listener für Login-Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Überprüfen, ob Felder leer sind
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Überprüfen der Login-Daten
                boolean isValid = databaseHelper.checkUser(username, password);
                if (isValid) {
                    // Login erfolgreich, zur MainActivity navigieren
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    // Add after successful login in LoginActivity.java
// Store username in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.apply();
                    finish(); // LoginActivity beenden
                } else {
                    // Login fehlgeschlagen
                    Toast.makeText(LoginActivity.this, "Ungültiger Benutzername oder Passwort", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener für Register-TextView
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zur RegisterActivity navigieren
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}