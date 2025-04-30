package com.example.lab03_bacha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Score extends AppCompatActivity {

    TextView tvScore;
    ProgressBar progressBar;
    Button bLogout, bTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Récupération des vues depuis le layout XML
        tvScore = findViewById(R.id.tvScore);
        progressBar = findViewById(R.id.progressBar);
        bLogout = findViewById(R.id.bLogout);
        bTry = findViewById(R.id.bTry);

        // Récupération du score total et du score max depuis l'intent
        int score = getIntent().getIntExtra("score", 0);
        int maxScore = getIntent().getIntExtra("maxScore", 5);

        // Calcul du pourcentage de réussite
        int percentage = (score * 100) / maxScore;

        // Affichage du pourcentage dans le TextView
        tvScore.setText(percentage + "%");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            SupabaseClient.envoyerScore(email, percentage);
        }


        // Mise à jour de la ProgressBar avec le score (fixe, non animée)
        progressBar.setMax(100); // On fixe le maximum à 100 pour correspondre au %
        progressBar.setProgress(percentage); // On affiche directement le pourcentage sans animation

        // Bouton "Logout" : retourne à la première activité (ou login si tu veux)
        bLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Score.this, Login.class); // à adapter si page de login différente
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Nettoie la pile d'activités
            startActivity(intent);
            finish();
        });

        // Bouton "Try again" : recommence le quiz depuis la première question
        bTry.setOnClickListener(v -> {
            Intent intent = new Intent(Score.this, Quiz1.class); // à adapter selon ta première page
            startActivity(intent);
            finish();
        });
    }
}
