package com.example.knowhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowhub.data.UserDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private UserDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialisierung der UI-Elemente
        editTextUsername = findViewById(R.id.editTextRegUsername);
        editTextEmail = findViewById(R.id.editTextRegEmail);
        editTextPassword = findViewById(R.id.editTextRegPassword);
        editTextConfirmPassword = findViewById(R.id.editTextRegConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Initialisierung des DatabaseHelpers
        databaseHelper = new UserDatabaseHelper(this);

        // Listener für Register-Button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                // Überprüfen, ob alle Felder ausgefüllt sind
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Überprüfen, ob die Passwörter übereinstimmen
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Überprüfen, ob der Benutzername bereits existiert
                if (databaseHelper.checkUsername(username)) {
                    Toast.makeText(RegisterActivity.this, "Benutzername bereits vergeben", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Überprüfen, ob die E-Mail bereits existiert
                if (databaseHelper.checkEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "E-Mail bereits registriert", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Benutzer registrieren
                boolean isSuccess = databaseHelper.registerUser(username, email, password);
                if (isSuccess) {
                    Toast.makeText(RegisterActivity.this, "Registrierung erfolgreich", Toast.LENGTH_SHORT).show();
                    // Zur LoginActivity navigieren
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // RegisterActivity beenden
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrierung fehlgeschlagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener für Login-TextView
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Zur LoginActivity navigieren
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
