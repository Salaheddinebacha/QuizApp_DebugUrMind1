package com.example.lab03_bacha;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    // Déclaration des champs de saisie (email, mot de passe, confirmation mot de passe)
    EditText etMail, etPassword, etPassword1;

    // Bouton d'inscription
    Button bRegister;

    // Instance FirebaseAuth pour gérer l'authentification
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lier cette activité à son layout XML
        setContentView(R.layout.activity_register);

        // Récupérer les composants UI via leurs IDs dans le layout
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etPassword1 = findViewById(R.id.etPass);
        bRegister = findViewById(R.id.bRegister);

        // Initialisation de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Gestionnaire du clic sur le bouton d'inscription
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les textes saisis par l'utilisateur et supprimer les espaces inutiles
                String mail = etMail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String password1 = etPassword1.getText().toString().trim();

                // Vérifier que tous les champs sont remplis
                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password1)) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return; // Arrêter le traitement si un champ est vide
                }

                // Vérifier que le mot de passe fait au moins 6 caractères
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
                    return; // Arrêter si mot de passe trop court
                }

                // Vérifier que les deux mots de passe sont identiques
                if (!password.equals(password1)) {
                    Toast.makeText(getApplicationContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                    return; // Arrêter si confirmation différente
                }

                // Utiliser Firebase pour créer un nouveau compte avec email et mot de passe
                mAuth.createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Si la création de compte réussit
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Inscription réussie !", Toast.LENGTH_LONG).show();
                                    // Rediriger vers la page de connexion
                                    startActivity(new Intent(Register.this, Login.class));
                                    finish(); // Fermer l'activité actuelle
                                } else {
                                    // Sinon afficher un message d'erreur avec la raison retournée par Firebase
                                    Toast.makeText(getApplicationContext(), "Échec de l'inscription : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
