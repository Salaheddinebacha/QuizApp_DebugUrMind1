package com.example.lab03_bacha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz2 extends AppCompatActivity {

    TextView optionJAVA, optionHtml, optionKotlin, optionCPLUS;
    Button bNext;

    String selectedAnswer = "";
    int score = 0;
    String correctAnswer = "JAVA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);

        // Récupération des éléments UI
        optionJAVA = findViewById(R.id.optionJAVA);
        optionHtml = findViewById(R.id.optionHtml);
        optionKotlin = findViewById(R.id.optionKotlin);
        optionCPLUS = findViewById(R.id.optionCPLUS);
        bNext = findViewById(R.id.bNext);

        // Récupérer le score précédent
        score = getIntent().getIntExtra("score", 0);

        // Listener commun pour sélectionner une réponse
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors();
                v.setBackgroundColor(getResources().getColor(R.color.yellow));
                selectedAnswer = ((TextView) v).getText().toString();
            }
        };

        optionJAVA.setOnClickListener(optionClickListener);
        optionHtml.setOnClickListener(optionClickListener);
        optionKotlin.setOnClickListener(optionClickListener);
        optionCPLUS.setOnClickListener(optionClickListener);

        // Bouton "Suivant" pour passer à la question suivante
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedAnswer.equals(correctAnswer)) {
                        score += 1;
                    }
                    Intent intent = new Intent(Quiz2.this, QUIZ3.class);
                    intent.putExtra("score", score);
                    intent.putExtra("maxScore", 5);
                    startActivity(intent);
                    overridePendingTransition(R.anim.exit, R.anim.entry);
                    finish();
                }
            }
        });
    }

    // Réinitialiser la couleur des options
    private void resetOptionColors() {
        optionJAVA.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionHtml.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionKotlin.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionCPLUS.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }
}
