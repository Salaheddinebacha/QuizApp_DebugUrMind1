package com.example.lab03_bacha;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Quiz1 extends AppCompatActivity {

    TextView optionJava, optionPython, optionLangC, optionJS;
    Button bNext, bMic;
    String selectedAnswer = "";
    int score = 0;
    String correctAnswer = "LANGAGE C";
    final int REQUEST_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);

        // Récupération des éléments du layout
        optionJava = findViewById(R.id.optionJava);
        optionPython = findViewById(R.id.optionPython);
        optionLangC = findViewById(R.id.optionLangC);
        optionJS = findViewById(R.id.optionJS);
        bNext = findViewById(R.id.bNext);
        bMic = findViewById(R.id.bMic); // bouton micro dans ton layout

        // Récupérer le score du quiz précédent
        score = getIntent().getIntExtra("score", 0);

        // Ajout des listeners aux options
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors();
                v.setBackgroundColor(getResources().getColor(R.color.yellow));
                selectedAnswer = ((TextView) v).getText().toString();
            }
        };

        optionJava.setOnClickListener(optionClickListener);
        optionPython.setOnClickListener(optionClickListener);
        optionLangC.setOnClickListener(optionClickListener);
        optionJS.setOnClickListener(optionClickListener);

        // Action bouton Suivant
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
                        score += 1;
                    }

                    Intent intent = new Intent(Quiz1.this, Quiz2.class);
                    intent.putExtra("score", score);
                    intent.putExtra("maxScore", 5);
                    startActivity(intent);
                    overridePendingTransition(R.anim.exit, R.anim.entry);
                    finish();
                }
            }
        });

        // Action bouton Micro
        bMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakNow();
            }
        });
    }

    private void speakNow() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRENCH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez maintenant...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Votre appareil ne supporte pas la reconnaissance vocale", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String voiceAnswer = result.get(0).toUpperCase();

                if (voiceAnswer.contains("C") || voiceAnswer.contains("LANGAGE C")) {
                    simulateClick(optionLangC);
                } else if (voiceAnswer.contains("JAVA")) {
                    simulateClick(optionJava);
                } else if (voiceAnswer.contains("PYTHON")) {
                    simulateClick(optionPython);
                } else if (voiceAnswer.contains("JAVASCRIPT") || voiceAnswer.contains("JS")) {
                    simulateClick(optionJS);
                } else {
                    Toast.makeText(this, "Votre Réponse non reconnue : " + voiceAnswer, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Simuler un clic sur une réponse
    private void simulateClick(TextView option) {
        resetOptionColors();
        option.setBackgroundColor(getResources().getColor(R.color.yellow));
        selectedAnswer = option.getText().toString();
    }

    // Remettre les couleurs initiales des options
    private void resetOptionColors() {
        optionJava.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionPython.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionLangC.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionJS.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }
}
