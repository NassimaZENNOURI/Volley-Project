package com.example.volleywithphpetudiant;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class modifyEtudiant extends AppCompatActivity {

    private EditText editNom, editPrenom, editVille;
    private Button btnUpdate;

    private String urlUpdate = "http://10.0.2.2/BackMobileVolley/ws/UpdateEtudiant.php";
    private String etudiantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_etudiant);

        // Récupérer les données de l'étudiant passées via l'Intent
        Intent intent = getIntent();
        etudiantId = intent.getStringExtra("id");
        String nom = intent.getStringExtra("nom");
        String prenom = intent.getStringExtra("prenom");
        String ville = intent.getStringExtra("ville");

        // Lier les champs de texte aux éléments de l'UI
        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editVille = findViewById(R.id.editVille);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Pré-remplir les champs avec les informations de l'étudiant
        editNom.setText(nom);
        editPrenom.setText(prenom);
        editVille.setText(ville);

        // Action de mise à jour lorsque l'utilisateur clique sur "Mettre à jour"
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEtudiant();
            }
        });
    }

    // Méthode pour envoyer la requête de mise à jour
    private void updateEtudiant() {
        final String nom = editNom.getText().toString().trim();
        final String prenom = editPrenom.getText().toString().trim();
        final String ville = editVille.getText().toString().trim();

        if (!nom.isEmpty() && !prenom.isEmpty() && !ville.isEmpty()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("la reponse du Update :" , response);
                            Toast.makeText(modifyEtudiant.this, "Mise à jour réussie", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(modifyEtudiant.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(modifyEtudiant.this, "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Paramètres envoyés avec la requête POST
                    Map<String, String> params = new HashMap<>();
                    params.put("id", etudiantId);
                    params.put("nom", nom);
                    params.put("prenom", prenom);
                    params.put("ville", ville);
                    return params;
                }
            };

            // Ajouter la requête à la file de requêtes de Volley
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(modifyEtudiant.this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
        }
    }
}
