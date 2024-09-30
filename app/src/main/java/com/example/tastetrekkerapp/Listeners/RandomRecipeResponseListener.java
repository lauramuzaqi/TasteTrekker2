package com.example.tastetrekkerapp.Listeners;

import com.example.tastetrekkerapp.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
