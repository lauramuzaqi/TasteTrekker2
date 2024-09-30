package com.example.tastetrekkerapp.Viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastetrekkerapp.R;

public class RandomRecipeViewHolder extends RecyclerView.ViewHolder {
    public CardView cvRecipeList;
    public TextView tvTitle;
    public TextView tvServings;
    public TextView tvTime;
    public ImageView imgVFood;
    public LinearLayout btnShare;

    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        cvRecipeList = itemView.findViewById(R.id.cvRecipeList);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvServings = itemView.findViewById(R.id.tvServings);
        tvTime = itemView.findViewById(R.id.tvTime);
        imgVFood = itemView.findViewById(R.id.imgVFood);
        btnShare = itemView.findViewById(R.id.btnShare);
    }
}
