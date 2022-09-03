package com.example.food_planr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    ArrayList<recipeModel> allRecipeModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the bottom nav bar
        bottomNavigationView = findViewById(R.id.bottomNavViewMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.recipes);

        //Adding action listener to the floating add recipes button
        FloatingActionButton addRecipe = findViewById(R.id.addRecipeButton);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, addRecipe.class));
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recipesList);
        setUpModels();
        recipesRecyclerViewAdpater adpater = new recipesRecyclerViewAdpater(this, allRecipeModels);
        recyclerView.setAdapter(adpater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (adpater.getItemCount() == 0){
            Toast.makeText(this, "No recipes found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.recipes:
               //startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.calendar:
                startActivity(new Intent(this, CalendarActivity.class));
                return true;

            case R.id.shoppingList:
                startActivity(new Intent(this, shoppingListActivity.class));
                return true;
        }
        return false;
    }

    private void setUpModels(){
        DataBaseHelper db = new DataBaseHelper(MainActivity.this);
        Cursor cursor = db.getAllRecipes();
        List<String> allTitles=new ArrayList<String>();
        List<String> allIngredients=new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                String title = cursor.getString(0);
                allTitles.add(title);
                String ingredients = cursor.getString(1);
                allIngredients.add(ingredients);
            }while (cursor.moveToNext());

            String[] allTitlesArray = new String[allTitles.size()];
            allTitlesArray = allTitles.toArray(allTitlesArray);

            String[] allIngredientsArray = new String[allIngredients.size()];
            allIngredientsArray = allIngredients.toArray(allIngredientsArray);

            for(int i =0; i<allTitlesArray.length; i++){
                allRecipeModels.add(new recipeModel(allTitlesArray[i],allIngredientsArray[i]));
            }
        }
    }
}