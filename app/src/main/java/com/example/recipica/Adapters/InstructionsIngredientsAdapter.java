package com.example.recipica.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipica.Models.Ingredient;
import com.example.recipica.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsIngredientsAdapter extends RecyclerView.Adapter<InstructionIngredientsViewHolder> {

    Context context;
    List<Ingredient> list;

    // Constructor
    public InstructionsIngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.list = ingredientList;
    }

    @NonNull
    @Override
    public InstructionIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_instructions_step_items, parent, false);
        return new InstructionIngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientsViewHolder holder, int position) {

        // Set ingredient name
        holder.textView_instructions_step_item.setText(list.get(position).name);
        holder.textView_instructions_step_item.setSelected(true);

        // Load ingredient image using Picasso
        Picasso.get()
                .load("https://spoonacular.com/cdn/ingredients_100x100/"+list.get(position).image)
                .into(holder.imageView_instructions_step_items);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionIngredientsViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView_instructions_step_items;
    TextView textView_instructions_step_item;

    public InstructionIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView_instructions_step_items = itemView.findViewById(R.id.imageView_instructions_step_items);
        textView_instructions_step_item = itemView.findViewById(R.id.textView_instruction_step_item);
    }
}
