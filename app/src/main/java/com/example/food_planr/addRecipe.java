package com.example.food_planr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class addRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        List<String> allIngredients=new ArrayList<String>();//Setting up a list that will store all the ingredients
        TextView ingredientInput = (TextView) findViewById(R.id.ingredientInput);
        Button addIngredientButton = (Button) findViewById(R.id.addIngredientButton);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentItem = ingredientInput.getText().toString();
                allIngredients.add(currentItem);
                ingredientInput.setText("");
                Toast.makeText(getApplicationContext(), "Ingredient added", Toast.LENGTH_SHORT).show();
            }
        });

        TextView recipeName = (TextView) findViewById(R.id.recipeTitleInput);
        Button saveButton = (Button) findViewById(R.id.saveRecipeButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = recipeName.getText().toString();
                if(!title.equals("")){
                    if(allIngredients.size()!=0){
                        String ingredients = "";
                        for(String item:allIngredients){
                            ingredients=ingredients+item+", ";
                        }
                        DataBaseHelper db = new DataBaseHelper(addRecipe.this);
                        boolean success = db.addRecipe(title,ingredients);
                        if(success){
                            Toast.makeText(getApplicationContext(), "Recipe Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(addRecipe.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{Toast.makeText(getApplicationContext(), "No ingredients have been saved", Toast.LENGTH_SHORT).show();}
                }
                else{Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();}


            }
        });


    }
}