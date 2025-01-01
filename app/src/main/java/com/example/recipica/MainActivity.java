package com.example.recipica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipica.Adapters.recipeAdapter;
import com.example.recipica.Listeners.RandomRecipeResponseListener;
import com.example.recipica.Listeners.RecipeClickListener;
import com.example.recipica.Models.RandomRecipeApiResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RequestManager manager;
    recipeAdapter recipeAdapter; // Adapter reference
    RecyclerView recyclerView;
    Spinner spinner;// RecyclerView reference
    List<String> tags = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ProgressDialog
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        searchView = findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener,tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.tags,
            R.layout.spinner_text
        );

        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);
        // Request data
        manager = new RequestManager(this);
//        manager.getRandomRecipes(randomRecipeResponseListener);
//        dialog.show();
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            // Initialize RecyclerView
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

            // Set up the adapter
            recipeAdapter = new recipeAdapter(MainActivity.this, response.recipes,recipeClickListener);
            recyclerView.setAdapter(recipeAdapter);

            dialog.dismiss(); // Dismiss ProgressDialog
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Dismiss ProgressDialog on error
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener,tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {

            startActivity(new Intent(MainActivity.this,RecipeDetailsActivity.class)
                    .putExtra("id",id));
        }
    };
}
