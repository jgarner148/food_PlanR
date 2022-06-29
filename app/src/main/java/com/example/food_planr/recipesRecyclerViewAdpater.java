package com.example.food_planr;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recipesRecyclerViewAdpater extends RecyclerView.Adapter<recipesRecyclerViewAdpater.MyViewHolder> {

    Context context;
    ArrayList<recipeModel> recipeModels;

    public recipesRecyclerViewAdpater(Context context, ArrayList<recipeModel> recipeModels){
        this.context=context;
        this.recipeModels=recipeModels;
    }

    @NonNull
    @Override
    public recipesRecyclerViewAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new recipesRecyclerViewAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recipesRecyclerViewAdpater.MyViewHolder holder, int position) {
            holder.title.setText(recipeModels.get(position).getTitle());
            holder.ingredents.setText(recipeModels.get(position).getIngredients());
    }

    @Override
    public int getItemCount() {
        return recipeModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, ingredents;
        Button deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextView);
            ingredents = itemView.findViewById(R.id.ingredientsTextView);
            deleteButton = itemView.findViewById(R.id.rcvDelButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHelper db = new DataBaseHelper(itemView.getContext());
                    boolean result = db.deleteRecipe(title.getText().toString());
                    if(result){
                        Toast.makeText(itemView.getContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                        recipeModels.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                    else{
                        Toast.makeText(itemView.getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}
