package com.example.lab03_bacha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz4 extends AppCompatActivity {

    TextView optionMatlab, optionR, optionTYPESCRIPT, optionCSS;
    Button bNext;
    String selectedAnswer = "";
    int score = 0;
    String correctAnswer = "TYPESCRIPT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz4);

        // Récupération des éléments du layout
        optionMatlab = findViewById(R.id.optionMatlab);
        optionR = findViewById(R.id.optionR);
        optionTYPESCRIPT = findViewById(R.id.optionTYPESCRIPT);
        optionCSS = findViewById(R.id.optionCSS);
        bNext = findViewById(R.id.bNext);

        // Récupérer le score du quiz précédent
        score = getIntent().getIntExtra("score", 0);

        // Ajout des listeners aux options
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors();
                v.setBackgroundColor(getResources().getColor(R.color.yellow)); // Highlight selection
                selectedAnswer = ((TextView) v).getText().toString();
            }
        };

        optionMatlab.setOnClickListener(optionClickListener);
        optionR.setOnClickListener(optionClickListener);
        optionTYPESCRIPT.setOnClickListener(optionClickListener);
        optionCSS.setOnClickListener(optionClickListener);

        // Action bouton Suivant
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedAnswer.equals(correctAnswer)) {
                        score += 1;
                    }

                    Intent intent = new Intent(Quiz4.this, Quiz5.class);
                    intent.putExtra("score", score);
                    intent.putExtra("maxScore", 5); // Tu peux adapter ça selon le total de questions
                    startActivity(intent);
                    overridePendingTransition(R.anim.exit, R.anim.entry);
                    finish();
                }
            }
        });
    }

    // Remettre les couleurs initiales des options
    private void resetOptionColors() {
        optionMatlab.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionR.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionTYPESCRIPT.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionCSS.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }
}