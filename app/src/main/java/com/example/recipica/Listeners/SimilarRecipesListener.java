package com.example.recipica.Listeners;

import com.example.recipica.Models.SimilarRecipeResponse;
import java.util.List;

public interface SimilarRecipesListener {
    void didFetch(List<SimilarRecipeResponse> response, String message);
    void didError(String message);
}
