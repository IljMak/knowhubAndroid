package com.example.knowhub.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.knowhub.R;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion;
    private TextView textViewQuestionCount;
    private RadioGroup radioGroupOptions;
    private RadioButton[] radioButtons;
    private Button buttonNext;

    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String quizTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialisiere UI-Elemente
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewQuestionCount = findViewById(R.id.textViewQuestionCount);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        buttonNext = findViewById(R.id.buttonNext);

        // RadioButtons finden und in Array speichern
        radioButtons = new RadioButton[4];
        radioButtons[0] = findViewById(R.id.radioButtonOption1);
        radioButtons[1] = findViewById(R.id.radioButtonOption2);
        radioButtons[2] = findViewById(R.id.radioButtonOption3);
        radioButtons[3] = findViewById(R.id.radioButtonOption4);

        // Das ausgewählte Thema aus dem Intent holen
        quizTopic = getIntent().getStringExtra("topic");
        if (quizTopic == null) {
            quizTopic = "android"; // Standard-Thema, falls keins ausgewählt wurde
        }

        // Titel basierend auf dem Thema setzen
        setTitle(getTopicTitle(quizTopic));

        // Quiz-Fragen basierend auf dem Thema laden
        loadQuizQuestions(quizTopic);

        // Erste Frage anzeigen
        displayQuestion(currentQuestionIndex);

        // Next-Button-Listener
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prüfen, ob eine Option ausgewählt wurde
                int selectedId = radioGroupOptions.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(QuizActivity.this,
                            getString(R.string.please_select_answer),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ermitteln, welche Option ausgewählt wurde
                int selectedOptionIndex = -1;
                for (int i = 0; i < radioButtons.length; i++) {
                    if (radioButtons[i].getId() == selectedId) {
                        selectedOptionIndex = i;
                        break;
                    }
                }




                // Zur nächsten Frage oder zum Ergebnis-Screen
                currentQuestionIndex++;
                if (currentQuestionIndex < quizQuestions.size()) {
                    // Nächste Frage anzeigen
                    displayQuestion(currentQuestionIndex);
                    // RadioGroup zurücksetzen
                    radioGroupOptions.clearCheck();
                } else {
                    // Quiz beendet, zur Ergebnisanzeige wechseln
                    Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("totalQuestions", quizQuestions.size());
                    intent.putExtra("topic", quizTopic);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // Diese Methode zeigt die aktuelle Frage an
    private void displayQuestion(int index) {
        QuizQuestion question = quizQuestions.get(index);
        textViewQuestion.setText(question.getQuestion());
        textViewQuestionCount.setText(getString(R.string.question_number,
                currentQuestionIndex + 1, quizQuestions.size()));

        String[] options = question.getOptions();
        for (int i = 0; i < radioButtons.length; i++) {
            if (i < options.length) {
                radioButtons[i].setText(options[i]);
                radioButtons[i].setVisibility(View.VISIBLE);
            } else {
                radioButtons[i].setVisibility(View.GONE);
            }
        }
    }

    // Diese Methode gibt den Titel des Themas zurück
    private String getTopicTitle(String topic) {
        switch (topic) {
            case "android":
                return "Android Grundlagen Quiz";
            case "programming":
                return "Allgemeine Programmierung Quiz";
            default:
                return "Quiz";
        }
    }

    // Diese Methode erstellt die Quiz-Fragen basierend auf dem ausgewählten Thema
    private void loadQuizQuestions(String topic) {
        quizQuestions = new ArrayList<>();

        if ("android".equals(topic)) {
            // Fragen zum Thema Android
            quizQuestions.add(new QuizQuestion(
                    "Welche Programmiersprache wird hauptsächlich für Android-Entwicklung verwendet?",
                    new String[]{"Java", "Swift", "C#", "PHP"},
                    0
            ));

            quizQuestions.add(new QuizQuestion(
                    "Was ist SQLite in Android?",
                    new String[]{"Ein Web-Service", "Eine Datenbank", "Ein UI-Element", "Ein Build-Tool"},
                    1
            ));

            quizQuestions.add(new QuizQuestion(
                    "Was ist ein Intent in Android?",
                    new String[]{"Eine Bildschirmauflösung", "Ein Messaging-Objekt", "Ein Datenbankindex", "Ein Layout"},
                    1
            ));

            quizQuestions.add(new QuizQuestion(
                    "Welches Layout ordnet Elemente in einer Reihe an?",
                    new String[]{"FrameLayout", "RelativeLayout", "LinearLayout", "TableLayout"},
                    2
            ));

            quizQuestions.add(new QuizQuestion(
                    "Was ist der Hauptzweck der AndroidManifest.xml-Datei?",
                    new String[]{
                            "Speicherung von Strings",
                            "Definition von Styles",
                            "Beschreibung der App-Komponenten und -Berechtigungen",
                            "Definition von Layouts"
                    },
                    2
            ));
        } else if ("programming".equals(topic)) {
            // Fragen zum Thema Allgemeine Programmierung
            quizQuestions.add(new QuizQuestion(
                    "Was ist ein Algorithmus?",
                    new String[]{
                            "Ein Programmfehler",
                            "Eine Schritt-für-Schritt-Anleitung zur Lösung eines Problems",
                            "Eine Programmiersprache",
                            "Ein Datentyp"
                    },
                    1
            ));

            quizQuestions.add(new QuizQuestion(
                    "Was bedeutet die Abkürzung 'API'?",
                    new String[]{
                            "Advanced Programming Interface",
                            "Application Programming Interface",
                            "Automated Programming Input",
                            "Application Process Integration"
                    },
                    1
            ));

            quizQuestions.add(new QuizQuestion(
                    "Was ist ein Compiler?",
                    new String[]{
                            "Ein Programm, das Code in Maschinensprache übersetzt",
                            "Ein Programm zur Fehlersuche",
                            "Eine Datenbank",
                            "Ein Betriebssystem"
                    },
                    0
            ));

            quizQuestions.add(new QuizQuestion(
                    "Was ist eine Schleife in der Programmierung?",
                    new String[]{
                            "Ein Fehler im Code",
                            "Eine Funktion, die sich selbst aufruft",
                            "Eine Struktur, die Code wiederholt ausführt",
                            "Ein Speicherbereich für Variablen"
                    },
                    2
            ));

            quizQuestions.add(new QuizQuestion(
                    "Was ist der Unterschied zwischen '==' und '===' in JavaScript?",
                    new String[]{
                            "Kein Unterschied, beide vergleichen Werte",
                            "'==' vergleicht Werte, '===' vergleicht Werte und Datentypen",
                            "'===' ist kein gültiger Operator",
                            "'==' vergleicht Referenzen, '===' vergleicht Werte"
                    },
                    1
            ));
        }
    }
}
