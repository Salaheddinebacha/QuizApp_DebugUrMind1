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

    EditText etMail, etPassword, etPassword1;
    Button bRegister;

    // Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        etPassword1 = findViewById(R.id.etPass);
        bRegister = findViewById(R.id.bRegister);

        // Initialisation Firebase
        mAuth = FirebaseAuth.getInstance();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = etMail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String password1 = etPassword1.getText().toString().trim();

                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password1)) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(password1)) {
                    Toast.makeText(getApplicationContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Création de compte avec Firebase
                mAuth.createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Inscription réussie !", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Register.this, Login.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Échec de l'inscription : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
