package com.example.tastetrekkerapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastetrekkerapp.Models.Recipe;
import com.example.tastetrekkerapp.R;
import com.example.tastetrekkerapp.Viewholders.FavoritesViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {
    private List<Recipe> favoriteRecipes;

    public FavoritesAdapter(List<Recipe> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_random_recipes, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Recipe recipe = favoriteRecipes.get(position);
        holder.tvTitle.setText(recipe.title);
        holder.tvServings.setText("Servings: " + recipe.servings);
        holder.tvTime.setText("Time: " + recipe.readyInMinutes + " mins");
        Picasso.get().load(recipe.image).into(holder.imgVFood);
    }

    @Override
    public int getItemCount() {
        return favoriteRecipes.size();
    }

}
