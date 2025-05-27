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

    // Déclaration des vues : affichage du score, barre de progression et boutons
    TextView tvScore;
    ProgressBar progressBar;
    Button bLogout, bTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score); // Liaison avec le layout XML

        // Récupération des composants UI par leurs IDs
        tvScore = findViewById(R.id.tvScore);
        progressBar = findViewById(R.id.progressBar);
        bLogout = findViewById(R.id.bLogout);
        bTry = findViewById(R.id.bTry);

        // Récupération des données transmises via l'intent : score obtenu et score maximal
        int score = getIntent().getIntExtra("score", 0);
        int maxScore = getIntent().getIntExtra("maxScore", 5);

        // Calcul du pourcentage de réussite
        int percentage = (score * 100) / maxScore;

        // Affichage du pourcentage dans le TextView
        tvScore.setText(percentage + "%");

        // Récupérer l'utilisateur connecté via Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            // Envoi du score vers Supabase (méthode externe que tu dois avoir définie)
            SupabaseClient.envoyerScore(email, percentage);
        }

        // Configuration de la ProgressBar
        progressBar.setMax(100);        // Maximum fixé à 100 (pourcentage)
        progressBar.setProgress(percentage);  // Remplissage de la barre selon le score, sans animation

        // Gestion du clic sur le bouton "Logout"
        bLogout.setOnClickListener(v -> {
            // Crée un intent vers l'activité de login (ou première activité)
            Intent intent = new Intent(Score.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Nettoyer la pile d'activités (pas de retour possible)
            startActivity(intent);
            finish(); // Terminer cette activité
        });

        // Gestion du clic sur le bouton "Try again" pour recommencer le quiz
        bTry.setOnClickListener(v -> {
            Intent intent = new Intent(Score.this, Quiz1.class); // Lancer la première question du quiz
            startActivity(intent);
            finish(); // Fermer cette activité
        });
    }
}
