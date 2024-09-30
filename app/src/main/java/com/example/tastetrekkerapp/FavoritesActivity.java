package com.example.tastetrekkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.tastetrekkerapp.Adapters.FavoritesAdapter;
import com.example.tastetrekkerapp.Models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private FavoritesManager favoritesManager;
    private RecyclerView rvFavorites;
    private FavoritesAdapter favoritesAdapter;
    private ImageButton btnBack;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        favoritesManager = new FavoritesManager(this);
        rvFavorites = findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        userId = FirebaseAuth.getInstance().getUid();
        loadFavorites();
    }

    private void loadFavorites() {
        new LoadFavoritesTask().execute();
    }

    private class LoadFavoritesTask extends AsyncTask<Void, Void, List<Recipe>> {
        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            return favoritesManager.getAllFavorites(userId);
        }

        @Override
        protected void onPostExecute(List<Recipe> favoriteRecipes) {
            favoritesAdapter = new FavoritesAdapter(favoriteRecipes);
            rvFavorites.setAdapter(favoritesAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        favoritesManager.close();
        super.onDestroy();
    }
}
