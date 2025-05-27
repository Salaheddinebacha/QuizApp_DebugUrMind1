package com.example.lab03_bacha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    // Déclaration des champs de saisie et boutons de l'interface
    EditText etLogin, etPassword;  // Champs pour l'email et le mot de passe
    Button bLogin;                 // Bouton pour lancer la connexion
    TextView tvRegister;           // Lien texte pour aller à l'inscription

    // Instance Firebase pour l'authentification
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Liaison avec le fichier XML qui décrit l'interface graphique
        setContentView(R.layout.activity_login);

        // Récupération des références des composants dans le layout via leurs IDs
        etLogin = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Initialisation de l'instance FirebaseAuth pour gérer l'authentification
        mAuth = FirebaseAuth.getInstance();

        // Gestionnaire de clic sur le bouton de connexion
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer les valeurs saisies par l'utilisateur, en supprimant les espaces inutiles
                String email = etLogin.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Vérifier que les deux champs ne sont pas vides
                if (email.isEmpty() || password.isEmpty()) {
                    // Afficher un message d'erreur si un champ est vide
                    Toast.makeText(Login.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                    return; // Arrêter le traitement ici
                }

                // Lancer la procédure d'authentification Firebase avec l'email et mot de passe
                mAuth.signInWithEmailAndPassword(email, password)
                        // Ajouter un listener pour récupérer le résultat de la tentative de connexion
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Si la connexion a réussi
                                if (task.isSuccessful()) {
                                    // Rediriger vers l'activité Localisation
                                    startActivity(new Intent(Login.this, Quiz1.class));
                                    // Finir cette activité pour ne pas pouvoir revenir en arrière
                                    finish();
                                } else {
                                    // Sinon, afficher un message d'erreur à l'utilisateur
                                    Toast.makeText(Login.this, "Email ou mot de passe incorrect !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Gestionnaire de clic sur le texte "S'inscrire"
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir l'activité d'inscription Register
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}
