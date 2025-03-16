package com.example.knowhub.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.knowhub.R;

public class QuizTopicsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_topics);

        // Titel setzen
        setTitle(getString(R.string.select_quiz_topic));

        // Topic 1: Android Grundlagen
        CardView cardViewAndroid = findViewById(R.id.cardViewTopic1);
        cardViewAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("android");
            }
        });

        // Topic 2: Allgemeine Programmierung
        CardView cardViewProgramming = findViewById(R.id.cardViewTopic2);
        cardViewProgramming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("programming");
            }
        });
    }

    private void startQuiz(String topic) {
        Intent intent = new Intent(QuizTopicsActivity.this, QuizActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }
}