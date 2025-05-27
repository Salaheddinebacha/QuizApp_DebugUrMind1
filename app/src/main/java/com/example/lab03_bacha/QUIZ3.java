package com.example.lab03_bacha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QUIZ3 extends AppCompatActivity {

    // Déclaration des options (TextView) et du bouton "Suivant"
    TextView optionSwift, optionCsharp , optionCPLUS, optionPhp;
    Button bNext;

    // Variable pour stocker la réponse sélectionnée par l'utilisateur
    String selectedAnswer = "";

    // Score cumulatif récupéré depuis les activités précédentes
    int score = 0;

    // Réponse correcte pour cette question
    String correctAnswer = "C++";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associer le layout XML de cette activité
        setContentView(R.layout.activity_quiz3);

        // Récupération des références des éléments UI via leurs IDs
        optionSwift = findViewById(R.id.optionSwift);
        optionCsharp = findViewById(R.id.optionCsharp);
        optionCPLUS = findViewById(R.id.optionCPLUS);
        optionPhp = findViewById(R.id.optionPhp);
        bNext = findViewById(R.id.bNext);

        // Récupération du score transmis par l'activité précédente
        score = getIntent().getIntExtra("score", 0);

        // Création d'un listener commun pour gérer la sélection d'une option
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors(); // Réinitialise les couleurs pour enlever l'ancien surlignage
                v.setBackgroundColor(getResources().getColor(R.color.yellow)); // Met en surbrillance l'option sélectionnée
                selectedAnswer = ((TextView) v).getText().toString(); // Enregistre la réponse choisie
            }
        };

        // Affecter le listener à toutes les options
        optionSwift.setOnClickListener(optionClickListener);
        optionCsharp.setOnClickListener(optionClickListener);
        optionCPLUS.setOnClickListener(optionClickListener);
        optionPhp.setOnClickListener(optionClickListener);

        // Listener pour le bouton "Suivant"
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer.isEmpty()) {
                    // Si aucune option n'est sélectionnée, afficher un message d'alerte
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    // Vérifier si la réponse sélectionnée est correcte
                    if (selectedAnswer.equals(correctAnswer)) {
                        score += 1; // Incrémenter le score
                    }

                    // Préparer l'intent pour passer à la question suivante (Quiz4)
                    Intent intent = new Intent(QUIZ3.this, Quiz4.class);
                    intent.putExtra("score", score); // Transférer le score mis à jour
                    intent.putExtra("maxScore", 5);  // Transférer le score maximal possible (pour info)

                    // Lancer l'activité suivante
                    startActivity(intent);

                    // Animation personnalisée de transition entre activités
                    overridePendingTransition(R.anim.exit, R.anim.entry);

                    // Fermer cette activité pour éviter de revenir en arrière
                    finish();
                }
            }
        });
    }

    /**
     * Remet toutes les options à leur couleur d'origine
     */
    private void resetOptionColors() {
        optionSwift.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionCsharp.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionCPLUS.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionPhp.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }
}
