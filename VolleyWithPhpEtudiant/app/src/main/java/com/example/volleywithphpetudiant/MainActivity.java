package com.example.volleywithphpetudiant;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleywithphpetudiant.adapter.EtudiantAdapter;
import com.example.volleywithphpetudiant.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainActivity extends AppCompatActivity{


    // Ajoutez ceci au début de la classe
    private RecyclerView recyclerView;
    private EtudiantAdapter etudiantAdapter;
    private List<Etudiant> etudiantList;
    RequestQueue requestQueue;
    String fetchUrl = "http://10.0.2.2/BackMobileVolley/ws/loadEtudiant.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser la file de requêtes Volley
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etudiantList = new ArrayList<>();
        //-----------------------------------------------------------------------
        StringRequest request = new StringRequest(Request.Method.POST, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response);
                    if (response != null && !response.isEmpty()) {
                        Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
                        List<Etudiant> etudiants = new Gson().fromJson(response, type);

                        if (etudiants != null) {
                            etudiantList.clear();
                            etudiantList.addAll(etudiants);
                            // Initialiser RecyclerView
                            recyclerView = findViewById(R.id.recyclerView);
                            etudiantAdapter = new EtudiantAdapter(MainActivity.this ,etudiantList);
                            recyclerView.setAdapter(etudiantAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            Log.d("teeest2 : ", String.valueOf(etudiantList.get(2).getNom()));
                        } else {
                            Log.e("Error", "Liste des étudiants vide ou mal formée");
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", "Erreur lors du parsing JSON", e);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                    }
                });

        requestQueue.add(request);
        //-------------------------------------------------------------------------------------------

        // À l'intérieur de votre méthode onCreate de MainActivity
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.add_menu_bar) {
                    Intent intent = new Intent(MainActivity.this, addEtudiant.class);
                    startActivity(intent);
                    return true;
                }if (id == R.id.search_bar) {

                    return true;
                }
                return false;
                //return super.onOptionsItemSelected(item);
            }
        });

    }

    private void fetchEtudiants() {
        StringRequest request = new StringRequest(Request.Method.POST, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response);
                    if (response != null && !response.isEmpty()) {
                        Type type = new TypeToken<Collection<Etudiant>>(){}.getType();
                        List<Etudiant> etudiants = new Gson().fromJson(response, type);

                        if (etudiants != null) {
                            etudiantList.clear();
                            etudiantList.addAll(etudiants);
                            etudiantAdapter.notifyDataSetChanged();
                            //etudiantAdapter.etudiantList.addAll(etudiants);
//                            etudiantAdapter.filtredList.clear();
//                            etudiantAdapter.filtredList.addAll(etudiantList);
//                            etudiantAdapter.notifyDataSetChanged();
                            Log.d("teeest2 : ", String.valueOf(etudiantList.get(2).getNom()));
                        } else {
                            Log.e("Error", "Liste des étudiants vide ou mal formée");
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error", "Erreur lors du parsing JSON", e);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                    }
                });

        requestQueue.add(request);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu called 1 ");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (etudiantAdapter != null){
                    etudiantAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return true;
    }



}
