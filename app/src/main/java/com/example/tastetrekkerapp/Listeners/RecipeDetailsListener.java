package com.example.tastetrekkerapp.Listeners;

import com.example.tastetrekkerapp.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse recipeDetailsResponse, String message);
    void didError(String message);
}
