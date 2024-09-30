package com.example.tastetrekkerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.tastetrekkerapp.Adapters.RandomRecipeAdapter;
import com.example.tastetrekkerapp.Listeners.RandomRecipeResponseListener;
import com.example.tastetrekkerapp.Listeners.RecipeClickListener;
import com.example.tastetrekkerapp.Models.RandomRecipeApiResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ImageButton btnLogOut;
    private TextView tvHello;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private RequestManager requestManager;
    private RandomRecipeAdapter randomRecipeAdapter;
    private RecyclerView recyclerView;
    private Toolbar userToolbar;
    private List<String> tags = new ArrayList<>();
    private SearchView search;
    private FloatingActionButton fabFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading ...");

        requestManager = new RequestManager(this);
        requestManager.getRandomRecipes(randomRecipeResponseListener, tags);
        progressDialog.show();

        userToolbar = findViewById(R.id.userToolbar);
        setSupportActionBar(userToolbar);

        recyclerView = findViewById(R.id.rv_random);
        search = findViewById(R.id.search);

        btnLogOut = findViewById(R.id.btnLogOut);
        tvHello = findViewById(R.id.tvHello);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        fabFavorites = findViewById(R.id.fabFavorites);

        fabFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboardActivity.this, FavoritesActivity.class));
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Clear SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear all data
                editor.apply(); // Apply changes*/

                firebaseAuth.signOut();
                checkUser();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                requestManager.getRandomRecipes(randomRecipeResponseListener, tags);
                progressDialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_user_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tags.clear();

        int itemId = item.getItemId();

        if (itemId == R.id.item_main_course) {
            tags.add("main course");
        } else if (itemId == R.id.item_side_dish) {
            tags.add("side dish");
        } else if (itemId == R.id.item_dessert) {
            tags.add("dessert");
        } else if (itemId == R.id.item_appetizer) {
            tags.add("appetizer");
        } else if (itemId == R.id.item_salad) {
            tags.add("salad");
        } else if (itemId == R.id.item_bread) {
            tags.add("bread");
        } else if (itemId == R.id.item_breakfast) {
            tags.add("breakfast");
        } else if (itemId == R.id.item_soup) {
            tags.add("soup");
        } else if (itemId == R.id.item_beverage) {
            tags.add("beverage");
        } else if (itemId == R.id.item_sauce) {
            tags.add("sauce");
        } else if (itemId == R.id.item_marinade) {
            tags.add("marinade");
        } else if (itemId == R.id.item_fingerfood) {
            tags.add("fingerfood");
        } else if (itemId == R.id.item_snack) {
            tags.add("snack");
        } else if (itemId == R.id.item_drink) {
            tags.add("drink");
        } else {
            return super.onOptionsItemSelected(item);
        }

        Snackbar.make(findViewById(android.R.id.content), "Fetching recipes for " + tags.get(0), Snackbar.LENGTH_SHORT).show();
        requestManager.getRandomRecipes(randomRecipeResponseListener, tags);
        progressDialog.show();

        return true;
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            tvHello.setText("Loading...");
            String uid = firebaseUser.getUid();

            databaseReference.child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue(String.class);
                    if (name != null && !name.isEmpty()) {
                        tvHello.setText("Hello " + name);
                    } else {
                        tvHello.setText("Hello User");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    tvHello.setText("Hello User");
                }
            });
        }
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            progressDialog.dismiss();
            recyclerView = findViewById(R.id.rv_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(UserDashboardActivity.this, 1));
            randomRecipeAdapter = new RandomRecipeAdapter(UserDashboardActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText( UserDashboardActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(UserDashboardActivity.this, RecipeDetailsActivity.class)
                    .putExtra("id", id));
        }
    };
}