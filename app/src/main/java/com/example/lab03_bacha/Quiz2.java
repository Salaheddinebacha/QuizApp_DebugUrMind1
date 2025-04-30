package com.example.lab03_bacha;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Quiz2 extends AppCompatActivity {

    TextView optionJAVA, optionHtml, optionKotlin, optionCPLUS;
    Button bNext, bTakePhoto;
    String selectedAnswer = "";
    int score = 0;
    String correctAnswer = "JAVA";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);


        // Récupération des éléments du layout
        optionJAVA = findViewById(R.id.optionJAVA);
        optionHtml = findViewById(R.id.optionHtml);
        optionKotlin = findViewById(R.id.optionKotlin);
        optionCPLUS = findViewById(R.id.optionCPLUS);
        bNext = findViewById(R.id.bNext);
        bTakePhoto = findViewById(R.id.bTakePhoto); // Assure-toi d'ajouter ce bouton dans ton XML

        score = getIntent().getIntExtra("score", 0);

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

        bTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissionAndTakePhoto();
            }
        });

    }

    private void resetOptionColors() {
        optionJAVA.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionHtml.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionKotlin.setBackgroundColor(getResources().getColor(R.color.grayLight));
        optionCPLUS.setBackgroundColor(getResources().getColor(R.color.grayLight));
    }

    private void uploadToSupabase(File imageFile) {
        String supabaseUrl = "https://gkdacjwhztkjgreotkdm.supabase.co";
        String supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdrZGFjandoenRramdyZW90a2RtIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0NTM1NjAyNSwiZXhwIjoyMDYwOTMyMDI1fQ.ECC1qEVXiw_cDM832-prjepbfNIaxZYzoL3eRUTIuFs";
        String bucketName = "photos"; // nom de ton bucket
        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));
        Request request = new Request.Builder()
                .url(supabaseUrl + "/storage/v1/object/" + bucketName + "/" + fileName)
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .addHeader("Content-Type", "image/jpeg")
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(Quiz2.this, "Échec de l'upload", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + fileName;
                    runOnUiThread(() -> Toast.makeText(Quiz2.this, "Upload réussi: " + publicUrl, Toast.LENGTH_LONG).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(Quiz2.this, "Erreur Supabase: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


    private static final int REQUEST_CAMERA_PERMISSION = 101;

    private void checkCameraPermissionAndTakePhoto() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permission caméra refusée !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erreur de fichier", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.lab03_bacha.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);


            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File imageFile = new File(currentPhotoPath);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Photo prise et enregistrée !", Toast.LENGTH_SHORT).show();
        }
        // Ajouter ici :
        uploadToSupabase(imageFile);

    }
}
