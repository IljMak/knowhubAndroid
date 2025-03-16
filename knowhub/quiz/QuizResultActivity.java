package com.example.knowhub.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import com.example.knowhub.data.UserDatabaseHelper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowhub.MainActivity;
import com.example.knowhub.R;

public class QuizResultActivity extends AppCompatActivity {

    private TextView textViewScore;
    private TextView textViewTopic;
    private Button buttonNewQuiz;
    private Button buttonNewTopic;
    private Button buttonHome;
    private String quizTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // Initialisiere UI-Elemente
        textViewScore = findViewById(R.id.textViewScore);
        textViewTopic = findViewById(R.id.textViewTopic);
        buttonNewQuiz = findViewById(R.id.buttonNewQuiz);
        buttonNewTopic = findViewById(R.id.buttonNewTopic);
        buttonHome = findViewById(R.id.buttonHome);

        // Add to onCreate in QuizResultActivity.java after displaying score

        // Daten aus Intent auslesen
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        quizTopic = getIntent().getStringExtra("topic");

        // Punktestand anzeigen
        textViewScore.setText(score + " / " + totalQuestions);

        // Themenname anzeigen
        String topicName = "Android Grundlagen";
        if ("programming".equals(quizTopic)) {
            topicName = "Allgemeine Programmierung";
        }
        textViewTopic.setText(topicName);

        // Button-Listener für neues Quiz im selben Thema
        buttonNewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, QuizActivity.class);
                intent.putExtra("topic", quizTopic);
                startActivity(intent);
                finish();
            }
        });

        // Button-Listener für Auswahl eines neuen Themas
        buttonNewTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, QuizTopicsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Button-Listener für Rückkehr zur Hauptseite
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        });

        // Add to onCreate in QuizResultActivity.java after displaying score


// Save quiz result to database
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(this);
// Get current username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest");

        dbHelper.saveQuizResult(username, quizTopic, totalQuestions, score);
    }
}