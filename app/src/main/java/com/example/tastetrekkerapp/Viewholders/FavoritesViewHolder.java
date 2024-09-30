package com.example.tastetrekkerapp.Viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastetrekkerapp.R;

public class FavoritesViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle, tvServings, tvTime;
    public ImageView imgVFood;

    public FavoritesViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvServings = itemView.findViewById(R.id.tvServings);
        tvTime = itemView.findViewById(R.id.tvTime);
        imgVFood = itemView.findViewById(R.id.imgVFood);
    }
}
