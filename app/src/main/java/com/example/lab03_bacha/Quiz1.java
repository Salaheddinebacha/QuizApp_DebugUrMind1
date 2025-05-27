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

    // Déclaration des options (TextViews) et des boutons
    TextView optionJava, optionPython, optionLangC, optionJS;
    Button bNext, bMic;

    // Variable pour stocker la réponse choisie par l'utilisateur
    String selectedAnswer = "";

    // Score cumulatif passé entre les activités
    int score = 0;

    // Réponse correcte à la question affichée
    String correctAnswer = "LANGAGE C";

    // Code de requête pour l'intent reconnaissance vocale
    final int REQUEST_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);  // Charge l’interface depuis le XML

        // Récupération des composants graphiques via leurs IDs dans le layout
        optionJava = findViewById(R.id.optionJava);
        optionPython = findViewById(R.id.optionPython);
        optionLangC = findViewById(R.id.optionLangC);
        optionJS = findViewById(R.id.optionJS);
        bNext = findViewById(R.id.bNext);
        bMic = findViewById(R.id.bMic); // Bouton micro pour la reconnaissance vocale

        // Récupère le score précédent passé par l'intent, 0 par défaut
        score = getIntent().getIntExtra("score", 0);

        // Listener commun pour gérer la sélection d'une option
        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors(); // Réinitialise les couleurs pour enlever l'ancien surlignage
                v.setBackgroundColor(getResources().getColor(R.color.yellow)); // Surligne l'option sélectionnée
                selectedAnswer = ((TextView) v).getText().toString(); // Stocke la réponse choisie
            }
        };

        // Affectation du listener à chaque option possible
        optionJava.setOnClickListener(optionClickListener);
        optionPython.setOnClickListener(optionClickListener);
        optionLangC.setOnClickListener(optionClickListener);
        optionJS.setOnClickListener(optionClickListener);

        // Listener pour le bouton "Suivant"
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer.isEmpty()) {
                    // Si aucune réponse n'a été sélectionnée, afficher un message d'alerte
                    Toast.makeText(getApplicationContext(), "Merci de choisir une réponse S.V.P !", Toast.LENGTH_SHORT).show();
                } else {
                    // Comparer la réponse choisie avec la bonne réponse (ignorant la casse)
                    if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
                        score += 1; // Incrémente le score si bonne réponse
                    }

                    // Passer à l'activité suivante Quiz2
                    Intent intent = new Intent(Quiz1.this, Quiz2.class);
                    intent.putExtra("score", score);   // Transférer le score mis à jour
                    intent.putExtra("maxScore", 5);    // Transférer score max (pour info)
                    startActivity(intent);

                    // Animation de transition personnalisée
                    overridePendingTransition(R.anim.exit, R.anim.entry);

                    // Fermer cette activité pour éviter de revenir en arrière
                    finish();
                }
            }
        });

        // Listener pour le bouton micro (reconnaissance vocale)
        bMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakNow();  // Lance la reconnaissance vocale
            }
        });
    }

    /**
     * Méthode pour démarrer la reconnaissance vocale avec un Intent spécifique
     */
    private void speakNow() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Utiliser un modèle libre de reconnaissance
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Définir la langue de reconnaissance : français
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRENCH);

        // Message affiché à l'utilisateur dans la boîte de dialogue vocale
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parlez maintenant...");

        try {
            // Démarrer l’activité et attendre un résultat (le texte reconnu)
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            // Si le téléphone ne supporte pas la reconnaissance vocale, afficher un message
            Toast.makeText(this, "Votre appareil ne supporte pas la reconnaissance vocale", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Méthode appelée à la fin de l'activité de reconnaissance vocale
     * @param requestCode code de requête
     * @param resultCode code du résultat (succès, échec)
     * @param data Intent contenant les données de retour
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Vérifier qu'on est bien dans le bon cas (reconnaissance vocale réussie)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {

            // Récupérer la liste des résultats possibles (textes reconnus)
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (result != null && result.size() > 0) {
                // Prendre le premier résultat et convertir en majuscule pour comparaison
                String voiceAnswer = result.get(0).toUpperCase();

                // Détection des réponses possibles via mots-clés dans la reconnaissance vocale
                if (voiceAnswer.contains("C") || voiceAnswer.contains("LANGAGE C")) {
                    simulateClick(optionLangC); // Sélectionner visuellement Langage C
                } else if (voiceAnswer.contains("JAVA")) {
                    simulateClick(optionJava);
                } else if (voiceAnswer.contains("PYTHON")) {
                    simulateClick(optionPython);
                } else if (voiceAnswer.contains("JAVASCRIPT") || voiceAnswer.contains("JS")) {
                    simulateClick(optionJS);
                } else {
                    // Réponse non reconnue dans les options, on affiche un message
                    Toast.makeText(this, "Votre Réponse non reconnue : " + voiceAnswer, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Simule un clic sur une option TextView, en la surlignant et en stockant la réponse choisie
     * @param option TextView représentant l'option à sélectionner
     */
    private void simulateClick(TextView option) {
        resetOptionColors(); // Réinitialiser la couleur de toutes les options
        option.setBackgroundColor(getResources().getColor(R.color.yellow)); // Surbrillance sur l'option
        selectedAnswer = option.getText().toString(); // Mise à jour de la réponse sélectionnée
    }

    /**
     * Remet la couleur de fond de toutes les options à la couleur par défaut
     */
    private void resetOptionColors() {
        optionJava.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionPython.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionLangC.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionJS.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }
}
