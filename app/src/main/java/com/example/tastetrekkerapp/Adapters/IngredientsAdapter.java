package com.example.tastetrekkerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastetrekkerapp.Models.ExtendedIngredient;
import com.example.tastetrekkerapp.R;
import com.example.tastetrekkerapp.Viewholders.IngredientsViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder>{
    Context context;
    List<ExtendedIngredient> list;

    public IngredientsAdapter(Context _context, List<ExtendedIngredient> _list) {
        this.context = _context;
        this.list = _list;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new IngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_recipe_ingredients, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder ingredientsViewHolder, int i) {
        ingredientsViewHolder.tvIngredient.setText(list.get(i).name);
        ingredientsViewHolder.tvIngredient.setSelected(true);
        ingredientsViewHolder.tvIngredientsQuantity.setText(list.get(i).original);
        ingredientsViewHolder.tvIngredientsQuantity.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+list.get(i).image).into(ingredientsViewHolder.ivIngredients);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}