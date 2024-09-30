package com.example.tastetrekkerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastetrekkerapp.Database.DatabaseHelper;
import com.example.tastetrekkerapp.Listeners.RecipeClickListener;
import com.example.tastetrekkerapp.Models.Recipe;
import com.example.tastetrekkerapp.Models.RecipeDetailsResponse;
import com.example.tastetrekkerapp.R;
import com.example.tastetrekkerapp.Viewholders.RandomRecipeViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder> {
    Context context;
    List<Recipe> list;
    RecipeClickListener recipeClickListener;

    public RandomRecipeAdapter(Context _context, List<Recipe> _list, RecipeClickListener _recipeClickListener) {
        this.context = _context;
        this.list = _list;
        this.recipeClickListener = _recipeClickListener;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder viewHolder, int i) {
        Recipe recipe = list.get(i);

        viewHolder.tvTitle.setText(list.get(i).title);
        viewHolder.tvTitle.setSelected(true);
        viewHolder.tvServings.setText(list.get(i).servings+" Servings");
        viewHolder.tvTime.setText(list.get(i).readyInMinutes+" Minutes");
        Picasso.get().load(list.get(i).image).into(viewHolder.imgVFood);

        viewHolder.cvRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeClickListener.onRecipeClicked(String.valueOf(list.get(viewHolder.getAdapterPosition()).id));
            }
        });

        viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plainInstructions = Html.fromHtml(recipe.instructions, Html.FROM_HTML_MODE_LEGACY).toString();
                String shareBody = "CHECK OUT THIS RECIPE :) " + "\n\n" + recipe.title + "\n\n" +
                        "Servings: " + recipe.servings + "\n\n" +
                        "Ready in: " + recipe.readyInMinutes + " minutes\n\n" +
                        "INSTRUCTIONS: " + "\n" + plainInstructions + "\n" + recipe.sourceUrl + "\n\n" +
                        "Enjoy cooking!";

                Picasso.get().load(recipe.image).into(new com.squareup.picasso.Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            File cachePath = new File(context.getCacheDir(), "images");
                            cachePath.mkdirs();
                            File file = new File(cachePath, "recipe_image.png");
                            FileOutputStream stream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            stream.close();

                            Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                            shareIntent.setPackage("com.whatsapp");

                            try {
                                context.startActivity(shareIntent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(context, "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(context, "Failed to load image.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
