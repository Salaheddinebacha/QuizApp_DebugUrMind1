package com.example.lab03_bacha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz5 extends AppCompatActivity {

    // Déclaration des options de réponses (TextView) et du bouton "Suivant"
    TextView optionSwift, optionJS, optionHtml, optionPhp;
    Button bNext;

    // Stockage de la réponse sélectionnée par l'utilisateur
    String selectedAnswer = "";

    // Score accumulé depuis les activités précédentes
    int score = 0;

    // Réponse correcte pour la question actuelle
    String correctAnswer = "SWIFT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associer l'activité à son layout XML
        setContentView(R.layout.activity_quiz5);

        // Récupération des composants graphiques via leurs IDs
        optionSwift = findViewById(R.id.optionSwift);
        optionJS = findViewById(R.id.optionJS);
        optionHtml = findViewById(R.id.optionHtml);
        optionPhp = findViewById(R.id.optionPhp);
        bNext = findViewById(R.id.bNext);

        // Récupération du score transmis depuis l'activité précédente
        score = getIntent().getIntExtra("score", 0);

        // Listener commun pour gérer la sélection d'une option
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors(); // Réinitialiser les couleurs des options (dé-surligner)
                v.setBackgroundColor(getResources().getColor(R.color.yellow)); // Surligner l'option sélectionnée
                selectedAnswer = ((TextView) v).getText().toString(); // Enregistrer la réponse sélectionnée
            }
        };

        // Assignation du listener aux options
        optionSwift.setOnClickListener(optionClickListener);
        optionJS.setOnClickListener(optionClickListener);
        optionHtml.setOnClickListener(optionClickListener);
        optionPhp.setOnClickListener(optionClickListener);

        // Listener pour le bouton "Suivant"
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer.isEmpty()) {
                    // Si aucune option n'a été sélectionnée, afficher un message d'alerte
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    // Vérifier si la réponse choisie est correcte
                    if (selectedAnswer.equals(correctAnswer)) {
                        score += 1; // Incrémenter le score
                    }

                    // Préparer l'intent pour passer à l'écran du score final
                    Intent intent = new Intent(Quiz5.this, Score.class);
                    intent.putExtra("score", score);    // Transmettre le score final
                    intent.putExtra("maxScore", 5);     // Transmettre le score maximal (pour information)

                    // Lancer l'activité Score
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
     * Remet la couleur de fond de toutes les options à la couleur par défaut
     */
    private void resetOptionColors() {
        optionSwift.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionJS.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionHtml.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionPhp.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }
}
