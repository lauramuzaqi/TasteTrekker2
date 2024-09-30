package com.example.tastetrekkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastetrekkerapp.Adapters.IngredientsAdapter;
import com.example.tastetrekkerapp.Listeners.RecipeDetailsListener;
import com.example.tastetrekkerapp.Models.Recipe;
import com.example.tastetrekkerapp.Models.RecipeDetailsResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RecipeDetailsActivity extends AppCompatActivity {
    private TextView tvRecipeName, tvRecipeSource, tvRecipeSummary, tvInstructions;
    private ImageView ivRecipeImage;
    private RecyclerView rvRecipeIngredients;
    private RequestManager requestManager;
    private IngredientsAdapter ingredientsAdapter;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private FloatingActionButton fabFavorite;
    private FavoritesManager favoritesManager;
    private Recipe currentRecipe;
    private String userId;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        
        findViews();
        favoritesManager = new FavoritesManager(this);

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        requestManager = new RequestManager(this);
        requestManager.getRecipeDetails(recipeDetailsListener, id);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading ...");
        progressDialog.show();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userId = FirebaseAuth.getInstance().getUid();

        fabFavorite.setOnClickListener(v -> {
            if (currentRecipe != null) {
                if (favoritesManager.isFavorite(userId, currentRecipe.id)) {
                    favoritesManager.removeFavorite(userId, currentRecipe.id);
                    fabFavorite.setImageResource(R.drawable.ic_favorite_border);
                    Toast.makeText(RecipeDetailsActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    favoritesManager.addFavorite(userId, currentRecipe);
                    fabFavorite.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(RecipeDetailsActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RecipeDetailsActivity.this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void findViews() {
        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvRecipeSource = findViewById(R.id.tvRecipeSource);
        tvRecipeSummary = findViewById(R.id.tvRecipeSummary);
        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        rvRecipeIngredients = findViewById(R.id.rvRecipeIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        fabFavorite = findViewById(R.id.fabFavorite);
        toolbar = findViewById(R.id.toolbar);
    }
    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse recipeDetailsResponse, String message) {
            progressDialog.dismiss();

            currentRecipe = new Recipe();
            currentRecipe.id = recipeDetailsResponse.id;
            currentRecipe.title = recipeDetailsResponse.title;
            currentRecipe.servings = recipeDetailsResponse.servings;
            currentRecipe.readyInMinutes = recipeDetailsResponse.readyInMinutes;
            currentRecipe.image = recipeDetailsResponse.image;

            tvRecipeName.setText(recipeDetailsResponse.title);
            tvRecipeSource.setText(recipeDetailsResponse.sourceName);
            tvRecipeSummary.setText(Html.fromHtml(recipeDetailsResponse.summary, Html.FROM_HTML_MODE_LEGACY).toString());
            tvInstructions.setText(Html.fromHtml(recipeDetailsResponse.instructions, Html.FROM_HTML_MODE_LEGACY).toString());

            Picasso.get().load(recipeDetailsResponse.image).into(ivRecipeImage);

            if (favoritesManager.isFavorite(String.valueOf(userId),currentRecipe.id)) {
                fabFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                fabFavorite.setImageResource(R.drawable.ic_favorite_border);
            }

            rvRecipeIngredients.setHasFixedSize(true);
            rvRecipeIngredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, recipeDetailsResponse.extendedIngredients);
            rvRecipeIngredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            progressDialog.dismiss();
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}