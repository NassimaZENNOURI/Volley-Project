package com.example.volleywithphpetudiant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleywithphpetudiant.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class addEtudiant extends AppCompatActivity implements View.OnClickListener {

    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton m;
    private RadioButton f;
    private Button add;
    RequestQueue requestQueue;
    String insertUrl = "http://10.0.2.2/BackMobileVolley/ws/createEtudiant.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_etudiant);

        // Initialisation des composants de l'interface utilisateur
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);
        add = findViewById(R.id.add);


        // Attacher le listener au bouton
        add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == add) {
            // Initialiser la file de requêtes Volley
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            // Vérifier si les champs sont bien remplis
            if (nom.getText().toString().isEmpty() || prenom.getText().toString().isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer une requête Volley
            StringRequest request = new StringRequest(Request.Method.POST, insertUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                            Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
                            Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                            for(Etudiant e : etudiants){
                                Log.d("TAG -> ", e.toString());
                            }
                            Intent intent = new Intent(addEtudiant.this, MainActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VolleyError", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //String sexe = m.isChecked() ? "homme" : "femme";
                    String sexe = "";
                    if (m.isChecked())
                        sexe = "homme";
                    else
                        sexe = "femme";

                    Map<String, String> params = new HashMap<>();
                    params.put("nom", nom.getText().toString());
                    params.put("prenom", prenom.getText().toString());
                    params.put("ville", ville.getSelectedItem().toString());
                    params.put("sexe", sexe);
                    return params;
                }
            };

            // Configurer le timeout de la requête pour éviter les ANR
            request.setRetryPolicy(new DefaultRetryPolicy(
                    5000,  // Timeout de 5 secondes
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,  // Nombre de tentatives
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT  // Multiplicateur de délai
            ));
            // Ajouter la requête à la file d'attente
            requestQueue.add(request);
        }
    }
}
