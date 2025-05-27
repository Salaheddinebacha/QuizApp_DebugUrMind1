package com.example.lab03_bacha;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Localisation extends AppCompatActivity implements OnMapReadyCallback {

    // Instance de GoogleMap pour manipuler la carte
    private GoogleMap mMap;

    // Client pour récupérer la localisation fusionnée (GPS + réseau)
    private FusedLocationProviderClient fusedLocationClient;

    // Code de requête pour la permission localisation
    private final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localisation); // Associe le layout XML

        // Initialisation du client pour obtenir la position GPS
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Récupération du fragment Google Maps dans le layout et demande la préparation de la carte
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Appel de onMapReady une fois la carte prête
        }

        // Bouton "Suivant" pour passer à la question 1 du quiz
        Button bNext = findViewById(R.id.bNext);
        bNext.setOnClickListener(v -> {
            Intent intent = new Intent(Localisation.this, Quiz1.class);
            startActivity(intent); // Lancer Quiz1
        });
    }

    /**
     * Méthode appelée lorsque la carte est prête à être utilisée
     * @param googleMap instance de GoogleMap prête
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Vérification des permissions localisation (GPS) au runtime
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Si permission non accordée, demander à l'utilisateur
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return; // Sortir car on attend la permission
        }

        // Activer le bouton "Ma position" sur la carte et affichage du point bleu
        mMap.setMyLocationEnabled(true);

        // Récupérer la dernière position connue de l'appareil
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // Afficher la position sur la carte avec un marqueur
                        showLocationOnMap(location);
                    } else {
                        // Message si la position ne peut être récupérée
                        Toast.makeText(this, "Impossible de récupérer la localisation", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Affiche un marqueur sur la carte à la position donnée et centre la caméra dessus
     * @param location position GPS
     */
    private void showLocationOnMap(Location location) {
        // Création d'un objet LatLng avec la latitude et longitude
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Ajouter un marqueur sur la carte
        mMap.addMarker(new MarkerOptions().position(latLng).title("Vous êtes ici"));

        // Déplacer la caméra vers la position avec un zoom rapproché
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    /**
     * Gestion de la réponse de l'utilisateur à la demande de permission localisation
     * @param requestCode code de requête
     * @param permissions permissions demandées
     * @param grantResults résultats accordés/refusés
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Vérifier que c'est la requête pour la localisation
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si permission accordée, on peut activer la localisation sur la carte
                onMapReady(mMap);
            } else {
                // Sinon, informer l'utilisateur que la permission a été refusée
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
