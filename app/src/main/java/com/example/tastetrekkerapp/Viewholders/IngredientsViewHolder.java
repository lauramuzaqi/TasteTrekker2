package com.example.tastetrekkerapp.Viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastetrekkerapp.R;

public class IngredientsViewHolder extends RecyclerView.ViewHolder{
    public TextView tvIngredientsQuantity, tvIngredient;
    public ImageView ivIngredients;
    public IngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        tvIngredientsQuantity = itemView.findViewById(R.id.tvIngredientsQuantity);
        tvIngredient = itemView.findViewById(R.id.tvIngredient);
        ivIngredients = itemView.findViewById(R.id.ivIngredients);
    }
}