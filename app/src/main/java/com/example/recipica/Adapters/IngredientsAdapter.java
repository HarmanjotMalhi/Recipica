package com.example.recipica.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipica.Models.ExtendedIngredient;
import com.example.recipica.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    Context context;
    List<ExtendedIngredient> extendedIngredients;

    public IngredientsAdapter(Context context, List<ExtendedIngredient> extendedIngredients) {
        this.context = context;
        this.extendedIngredients = extendedIngredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_meal_ingredients, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        ExtendedIngredient ingredient = extendedIngredients.get(position);

        Log.d("IngredientsAdapter", "Binding: " + ingredient.name + ", Position: " + position);

        holder.textView_ingredients_name.setText(ingredient.name);
        holder.textView_ingredients_name.setSelected(true);
        holder.textView_ingredients_quantity.setText(ingredient.amount + " " + ingredient.unit);
        holder.textView_ingredients_quantity.setSelected(true);

        Picasso.get()
                .load("https://spoonacular.com/cdn/ingredients_100x100/" + ingredient.image)
                .into(holder.imageView_ingredients);
    }

    @Override
    public int getItemCount() {
        return extendedIngredients.size();
    }

    public static class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView textView_ingredients_name, textView_ingredients_quantity;
        ImageView imageView_ingredients;

        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_ingredients_name = itemView.findViewById(R.id.textView_ingredients_name);
            textView_ingredients_quantity = itemView.findViewById(R.id.textView_ingredients_quantity);
            imageView_ingredients = itemView.findViewById(R.id.imageView_ingredients);
        }
    }
}
