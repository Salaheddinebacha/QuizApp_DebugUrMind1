package com.example.lab03_bacha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz4 extends AppCompatActivity {

    // Déclaration des options de réponse (TextView) et du bouton "Suivant"
    TextView optionMatlab, optionR, optionTYPESCRIPT, optionCSS;
    Button bNext;

    // Variable pour stocker la réponse sélectionnée
    String selectedAnswer = "";

    // Score cumulé récupéré des activités précédentes
    int score = 0;

    // Réponse correcte pour la question actuelle
    String correctAnswer = "TYPESCRIPT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Liaison avec le layout XML de cette activité
        setContentView(R.layout.activity_quiz4);

        // Récupération des composants UI via leurs IDs
        optionMatlab = findViewById(R.id.optionMatlab);
        optionR = findViewById(R.id.optionR);
        optionTYPESCRIPT = findViewById(R.id.optionTYPESCRIPT);
        optionCSS = findViewById(R.id.optionCSS);
        bNext = findViewById(R.id.bNext);

        // Récupération du score transmis depuis l'activité précédente
        score = getIntent().getIntExtra("score", 0);

        // Listener commun pour gérer la sélection d'une option
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors(); // Réinitialiser les couleurs des options
                v.setBackgroundColor(getResources().getColor(R.color.yellow)); // Mettre en surbrillance l'option sélectionnée
                selectedAnswer = ((TextView) v).getText().toString(); // Stocker la réponse choisie
            }
        };

        // Attribution du listener aux options
        optionMatlab.setOnClickListener(optionClickListener);
        optionR.setOnClickListener(optionClickListener);
        optionTYPESCRIPT.setOnClickListener(optionClickListener);
        optionCSS.setOnClickListener(optionClickListener);

        // Listener pour le bouton "Suivant"
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer.isEmpty()) {
                    // Si aucune option sélectionnée, afficher un message à l'utilisateur
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    // Vérifier si la réponse choisie est correcte
                    if (selectedAnswer.equals(correctAnswer)) {
                        score += 1; // Incrémenter le score
                    }

                    // Préparer l'intent pour passer à la question suivante (Quiz5)
                    Intent intent = new Intent(Quiz4.this, Quiz5.class);
                    intent.putExtra("score", score); // Transmettre le score mis à jour
                    intent.putExtra("maxScore", 5);  // Transmettre le score maximal (information)

                    // Démarrer l'activité suivante
                    startActivity(intent);

                    // Animation personnalisée de transition entre activités
                    overridePendingTransition(R.anim.exit, R.anim.entry);

                    // Fermer cette activité pour empêcher retour en arrière
                    finish();
                }
            }
        });
    }

    /**
     * Réinitialiser la couleur de fond de toutes les options à la couleur par défaut
     */
    private void resetOptionColors() {
        optionMatlab.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionR.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionTYPESCRIPT.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionCSS.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }
}
