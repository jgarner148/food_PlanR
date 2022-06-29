package com.example.food_planr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class shoppingListActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        bottomNavigationView = findViewById(R.id.bottomNavViewShoppingList);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.shoppingList);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.recipes:
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.calendar:
                startActivity(new Intent(this, CalendarActivity.class));
                return true;

            case R.id.shoppingList:
                //startActivity(new Intent(this, shoppingListActivity.class));
                return true;
        }
        return false;
    }
}