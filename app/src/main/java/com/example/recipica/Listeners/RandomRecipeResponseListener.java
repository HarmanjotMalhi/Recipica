package com.example.recipica.Listeners;

import com.example.recipica.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {

    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
