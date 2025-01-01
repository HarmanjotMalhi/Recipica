package com.example.recipica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipica.Adapters.IngredientsAdapter;
import com.example.recipica.Adapters.InstructionsAdapter;
import com.example.recipica.Adapters.SimilarRecipeAdapter;
import com.example.recipica.Listeners.InstructionsListener;
import com.example.recipica.Listeners.RecipeClickListener;
import com.example.recipica.Listeners.RecipeDetailsListener;
import com.example.recipica.Listeners.SimilarRecipesListener;
import com.example.recipica.Models.InstructionsResponse;
import com.example.recipica.Models.RecipeDetailsResponse;
import com.example.recipica.Models.SimilarRecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_similar, recycler_meal_instructions;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        // Fetch recipe details
        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getSimilarRecipes(similarRecipesListener, id);
        manager.getInstructions(instructionsListener, id);
        // Show ProgressDialog
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details....");
        dialog.show();
    }

    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.text_meal_source);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_similar = findViewById(R.id.recycler_meal_similar);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            Log.d("RecipeDetails", "Summary: " + response.summary);

            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);

            // Render HTML content with backward compatibility
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                textView_meal_summary.setText(Html.fromHtml(response.summary, Html.FROM_HTML_MODE_COMPACT));
            } else {
                textView_meal_summary.setText(Html.fromHtml(response.summary));
            }
            textView_meal_summary.setMovementMethod(LinkMovementMethod.getInstance());

            Picasso.get().load(response.image).into(imageView_meal_image);

            // Debug log to verify ingredients list
            Log.d("IngredientsList", "Total Ingredients: " + response.extendedIngredients.size());
            for (int i = 0; i < response.extendedIngredients.size(); i++) {
                Log.d("Ingredient", "Name: " + response.extendedIngredients.get(i).name +
                        ", Quantity: " + response.extendedIngredients.get(i).amount + " " +
                        response.extendedIngredients.get(i).unit);
            }

            // Setup RecyclerView for Ingredients as Horizontal
            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(
                    new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false)
            );

            // Attach adapter with lowercase "extendedIngredients"
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);
            ingredientsAdapter.notifyDataSetChanged();
        }

        @Override
        public void didError(String message) {
            dialog.dismiss();
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };



    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {
            recycler_meal_similar.setHasFixedSize(true);
            recycler_meal_similar.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailsActivity.this, response, recipeClickListener);
            recycler_meal_similar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class)
                    .putExtra("id", id));
        }
    };

    private final InstructionsListener  instructionsListener = new InstructionsListener(){
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this,  response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };
}
