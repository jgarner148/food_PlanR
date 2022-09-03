package com.example.food_planr;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Setting up the bottom nav view bar
        bottomNavigationView = findViewById(R.id.bottomNavViewCalendar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.calendar);

        //Check user exists
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        DataBaseHelper db = new DataBaseHelper(this);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        boolean needsUpdate = false;
        if(!db.checkUser()){
            db.createUser(calendar.get(Calendar.DAY_OF_MONTH));
            needsUpdate=true;
        }
        String[] currentUser = db.getUser();
        if(calendar.get(Calendar.DAY_OF_MONTH) >Integer.parseInt(currentUser[1]) || needsUpdate){
            String monDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            String tueDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            String wedDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            String thuDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            String friDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            String satDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            String sunDate = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

            boolean success = db.newWeek(monDate, tueDate, wedDate, thuDate, friDate,satDate,sunDate);
            if(success){
                Toast.makeText(this, "New Week Added", Toast.LENGTH_SHORT).show();
                currentUser=db.getUser();
            }
            else{
                Toast.makeText(this, "Error Adding New Week", Toast.LENGTH_SHORT).show();
            }
        }

        ContentValues calData = db.getCalInfo(currentUser[0]);
        if(calData.get("result").equals("fail")){
            Toast.makeText(this, "Error Getting Calendar Data", Toast.LENGTH_SHORT).show();
        }
        else{
            //Adding the Date to each day of the week on the calendar
            TextView monDateTV = (TextView) findViewById(R.id.monDate);
            monDateTV.setText(calData.get("monDate").toString());

            TextView tuesDateTV = (TextView) findViewById(R.id.tuesDate);
            tuesDateTV.setText(calData.get("tueDate").toString());

            TextView wedDateTV = (TextView) findViewById(R.id.wedDate);
            wedDateTV.setText(calData.get("wedDate").toString());

            TextView thursDateTV = (TextView) findViewById(R.id.thursDate);
            thursDateTV.setText(calData.get("thuDate").toString());

            TextView friDateTV = (TextView) findViewById(R.id.friDate);
            friDateTV.setText(calData.get("friDate").toString());

            TextView satDateTV = (TextView) findViewById(R.id.satDate);
            satDateTV.setText(calData.get("satDate").toString());

            TextView sunDateTV = (TextView) findViewById(R.id.sunDate);
            sunDateTV.setText(calData.get("sunDate").toString());
        }

        //Populating the textViews
        TextView monMealText = (TextView) findViewById(R.id.mondayMealText);
        TextView tuesMealText = (TextView) findViewById(R.id.tuesdayMealText);
        TextView wedMealText = (TextView) findViewById(R.id.wednesdayMealText);
        TextView thurTextMeal = (TextView) findViewById(R.id.thursdayMealText);
        TextView friMealText = (TextView) findViewById(R.id.fridayMealText);
        TextView satMealText = (TextView) findViewById(R.id.saturdayMealText);
        TextView sunMealText = (TextView) findViewById(R.id.sundayMealText);

        //Setting content of text views to noMeals if the user has not added a meal to that day yet
        String noMeals = "No Meals Added";
        //Monday
        if(calData.get("monRes").toString().equals("")){
            monMealText.setText(noMeals);
        }
        else{
            monMealText.setText(calData.get("monRes").toString());
        }
        //Tuesday
        if(calData.get("tueRes").toString().equals("")){
            tuesMealText.setText(noMeals);
        }
        else{
            tuesMealText.setText(calData.get("tueRes").toString());
        }
        //Wednesday
        if(calData.get("wedRes").toString().equals("")){
            wedMealText.setText(noMeals);
        }
        else{
            wedMealText.setText(calData.get("wedRes").toString());;
        }
        //Thursday
        if(calData.get("thuRes").toString().equals("")){
            thurTextMeal.setText(noMeals);
        }
        else{
            thurTextMeal.setText(calData.get("thuRes").toString());
        }
        //Friday
        if(calData.get("friRes").toString().equals("")){
            friMealText.setText(noMeals);
        }
        else{
            friMealText.setText(calData.get("friRes").toString());
        }
        //Saturday
        if(calData.get("satRes").toString().equals("")){
            satMealText.setText(noMeals);
        }
        else{
            satMealText.setText(calData.get("satRes").toString());
        }
        //Sunday
        if(calData.get("sunRes").toString().equals("")){
            sunMealText.setText(noMeals);
        }
        else{
            sunMealText.setText(calData.get("sunRes").toString());
        }

        //Setting up the delete buttons for each day
        Button mondayDelButton = (Button) findViewById(R.id.mondayDeleteButton);
        if(!monMealText.getText().toString().equals(noMeals)){
            mondayDelButton.setVisibility(View.VISIBLE);
        }
        mondayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(monMealText.getText().toString(),db,"monday");
            }
        });

        Button tuesdayDelButton = (Button) findViewById(R.id.tuesdayDeleteButton);
        if(!tuesMealText.getText().toString().equals(noMeals)){
            tuesdayDelButton.setVisibility(View.VISIBLE);
        }
        tuesdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(tuesMealText.getText().toString(),db,"tuesday");
            }
        });

        Button wednesdayDelButton = (Button) findViewById(R.id.wednesdayDeleteButton);
        if(!wedMealText.getText().toString().equals(noMeals)){
            wednesdayDelButton.setVisibility(View.VISIBLE);
        }
        wednesdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(wedMealText.getText().toString(),db,"wednesday");
            }
        });

        Button thursdayDelButton = (Button) findViewById(R.id.thursdayDeleteButton);
        if(!thurTextMeal.getText().toString().equals(noMeals)){
            thursdayDelButton.setVisibility(View.VISIBLE);
        }
        thursdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(thurTextMeal.getText().toString(),db,"thursday");
            }
        });

        Button fridayDelButton = (Button) findViewById(R.id.fridayDeleteButton);
        if(!friMealText.getText().toString().equals(noMeals)){
            fridayDelButton.setVisibility(View.VISIBLE);
        }
        fridayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(friMealText.getText().toString(),db,"friday");
            }
        });

        Button saturdayDelButton = (Button) findViewById(R.id.saturdayDeleteButton);
        if(!satMealText.getText().toString().equals(noMeals)){
            saturdayDelButton.setVisibility(View.VISIBLE);
        }
        saturdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(satMealText.getText().toString(),db,"saturday");
            }
        });

        Button sundayDelButton = (Button) findViewById(R.id.sundayDeleteButton);
        if(!sunMealText.getText().toString().equals(noMeals)){
            sundayDelButton.setVisibility(View.VISIBLE);
        }
        sundayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(sunMealText.getText().toString(),db,"sunday");
            }
        });

        //Creating the action to add a meal to each of the days
        //MONDAY
        Button mondayButton = (Button) findViewById(R.id.mondayMealAdd);
        String currentWeek = currentUser[0];
        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu monMenu = new PopupMenu(CalendarActivity.this, view);
                Cursor allRecipes = db.getAllRecipes();
                if (allRecipes.moveToFirst()) {
                    do {
                        monMenu.getMenu().add(allRecipes.getString(0));
                    } while (allRecipes.moveToNext());
                    monMenu.show();
                    monMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(monMealText.getText().toString().equals(noMeals)){
                                db.addMealToCal(currentWeek,"", "MONDAY", menuItem.getTitle().toString());
                            }
                            else{
                                db.addMealToCal(currentWeek,monMealText.getText().toString(), "MONDAY", menuItem.getTitle().toString());
                            }
                            mondayDelButton.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(getIntent());
                            return false;
                        }
                    });
                }
                else{
                    Toast.makeText(CalendarActivity.this, "Please add recipes first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //TUESDAY
        Button tuesdayButton = (Button) findViewById(R.id.tuesdayMealAdd);
        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu tuesMenu = new PopupMenu(CalendarActivity.this, view);
                Cursor allRecipes = db.getAllRecipes();
                if (allRecipes.moveToFirst()) {
                    do {
                        tuesMenu.getMenu().add(allRecipes.getString(0));
                    } while (allRecipes.moveToNext());
                    tuesMenu.show();
                    tuesMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(tuesMealText.getText().toString().equals(noMeals)){
                                db.addMealToCal(currentWeek,"", "TUESDAY", menuItem.getTitle().toString());
                            }
                            else{
                                db.addMealToCal(currentWeek,tuesMealText.getText().toString(), "TUESDAY", menuItem.getTitle().toString());
                            }
                            tuesdayDelButton.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(getIntent());
                            return false;
                        }
                    });
                }
                else{
                    Toast.makeText(CalendarActivity.this, "Please add recipes first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //WEDNESDAY
        Button wednesdayButton = (Button) findViewById(R.id.wednesdayMealAdd);
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu wedMenu = new PopupMenu(CalendarActivity.this, view);
                Cursor allRecipes = db.getAllRecipes();
                if (allRecipes.moveToFirst()) {
                    do {
                        wedMenu.getMenu().add(allRecipes.getString(0));
                    } while (allRecipes.moveToNext());

                    wedMenu.show();
                    wedMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(wedMealText.getText().toString().equals(noMeals)){
                                db.addMealToCal(currentWeek,"", "WEDNESDAY", menuItem.getTitle().toString());
                            }
                            else{
                                db.addMealToCal(currentWeek,wedMealText.getText().toString(), "WEDNESDAY", menuItem.getTitle().toString());
                            }
                            wednesdayDelButton.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(getIntent());
                            return false;
                        }
                    });
                }
                else{
                    Toast.makeText(CalendarActivity.this, "Please add recipes first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //THURSDAY
        Button thursdayButton = (Button) findViewById(R.id.thursdayMealAdd);
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu thurMenu = new PopupMenu(CalendarActivity.this, view);
                Cursor allRecipes = db.getAllRecipes();
                if (allRecipes.moveToFirst()) {
                    do {
                        thurMenu.getMenu().add(allRecipes.getString(0));
                    } while (allRecipes.moveToNext());

                    thurMenu.show();
                    thurMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                                if(thurTextMeal.getText().toString().equals(noMeals)){
                                db.addMealToCal(currentWeek,"", "THURSDAY", menuItem.getTitle().toString());
                            }
                            else{
                                db.addMealToCal(currentWeek,thurTextMeal.getText().toString(), "THURSDAY", menuItem.getTitle().toString());
                            }
                            thursdayDelButton.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(getIntent());
                            return false;
                        }
                    });
                }
                else{
                    Toast.makeText(CalendarActivity.this, "Please add recipes first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //FRIDAY
        Button fridayButton = (Button) findViewById(R.id.fridayMealAdd);
        fridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu friMenu = new PopupMenu(CalendarActivity.this, view);
                Cursor allRecipes = db.getAllRecipes();
                if (allRecipes.moveToFirst()) {
                    do {
                        friMenu.getMenu().add(allRecipes.getString(0));
                    } while (allRecipes.moveToNext());

                    friMenu.show();
                    friMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(friMealText.getText().toString().equals(noMeals)){
                                db.addMealToCal(currentWeek,"", "FRIDAY", menuItem.getTitle().toString());
                            }
                            else{
                                db.addMealToCal(currentWeek,friMealText.getText().toString(), "FRIDAY", menuItem.getTitle().toString());
                            }
                            fridayDelButton.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(getIntent());
                            return false;
                        }
                    });
                }
                else{
                    Toast.makeText(CalendarActivity.this, "Please add recipes first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SATURDAY
        Button saturdayButton = (Button) findViewById(R.id.saturdayMealAdd);
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu satMenu = new PopupMenu(CalendarActivity.this, view);
                Cursor allRecipes = db.getAllRecipes();
                if (allRecipes.moveToFirst()) {
                    do {
                        satMenu.getMenu().add(allRecipes.getString(0));
                    } while (allRecipes.moveToNext());

                    satMenu.show();
                    satMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(satMealText.getText().toString().equals(noMeals)){
                                db.addMealToCal(currentWeek,"", "SATURDAY", menuItem.getTitle().toString());
                            }
                            else{
                                db.addMealToCal(currentWeek,satMealText.getText().toString(), "SATURDAY", menuItem.getTitle().toString());
                            }
                            saturdayDelButton.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(getIntent());
                            return false;
                        }
                    });
                }
                else{
                    Toast.makeText(CalendarActivity.this, "Please add recipes first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SUNDAY
        Button sundayButton = (Button) findViewById(R.id.sundayMealAdd);
        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu sunMenu = new PopupMenu(CalendarActivity.this, view);
                Cursor allRecipes = db.getAllRecipes();
                if (allRecipes.moveToFirst()) {
                    do {
                        sunMenu.getMenu().add(allRecipes.getString(0));
                    } while (allRecipes.moveToNext());

                    sunMenu.show();
                    sunMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(sunMealText.getText().toString().equals(noMeals)){
                                db.addMealToCal(currentWeek,"", "SUNDAY", menuItem.getTitle().toString());
                            }
                            else{
                                db.addMealToCal(currentWeek,sunMealText.getText().toString(), "SUNDAY", menuItem.getTitle().toString());
                            }
                            sundayDelButton.setVisibility(View.VISIBLE);
                            finish();
                            startActivity(getIntent());
                            return false;
                        }
                    });
                }
                else{
                    Toast.makeText(CalendarActivity.this, "Please add recipes first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDelOptions(String allRecipes, DataBaseHelper db, String dayOfWeek){
        final String[] selection = {""}; //String stored this way as it has to be final
        String[] recipesArraywithBlank = allRecipes.split("\n");
        String[] recipesArraywithoutBlank = Arrays.copyOfRange(recipesArraywithBlank,1,recipesArraywithBlank.length);//removing the first element from the array as this is always blank
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Meal from Calendar");
        builder.setSingleChoiceItems(recipesArraywithoutBlank, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selection[0] = recipesArraywithoutBlank[i]; //Storing the string in the final array
            }
        });

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(selection[0].equals("")){
                    Toast.makeText(CalendarActivity.this, "No meal selected. \nNothing was deleted", Toast.LENGTH_SHORT).show();
                }
                else {
                    String[] currentUser = db.getUser();
                    String weekID = currentUser[0];
                    ContentValues result = db.getCalInfo(weekID);

                    String[] currentMeals;
                    if (dayOfWeek.equals("monday")) {
                        String mealsString = result.get("monRes").toString();
                        currentMeals = mealsString.split("\n");
                    } else if (dayOfWeek.equals("tuesday")) {
                        String mealsString = result.get("tueRes").toString();
                        currentMeals = mealsString.split("\n");
                    } else if (dayOfWeek.equals("wednesday")) {
                        String mealsString = result.get("wedRes").toString();
                        currentMeals = mealsString.split("\n");
                    } else if (dayOfWeek.equals("thursday")) {
                        String mealsString = result.get("thuRes").toString();
                        currentMeals = mealsString.split("\n");
                    } else if (dayOfWeek.equals("friday")) {
                        String mealsString = result.get("friRes").toString();
                        currentMeals = mealsString.split("\n");
                    } else if (dayOfWeek.equals("saturday")) {
                        String mealsString = result.get("satRes").toString();
                        currentMeals = mealsString.split("\n");
                    } else {
                        String mealsString = result.get("sunRes").toString();
                        currentMeals = mealsString.split("\n");
                    }

                    String[] newMeals = new String[currentMeals.length - 1];
                    int x = 0; //counter for newMeals array
                    Boolean done = false; //bool to make it only remove the first instance of a meal

                    for (int j = 0; j <= currentMeals.length - 1; j++) {
                        if (currentMeals[j].equals(selection[0]) && !done) {
                            done = true;
                        } else {
                            newMeals[x] = currentMeals[j];
                            x++;
                        }
                    }

                    String newMealsString = String.join("\n", newMeals);
                    db.updateMeals(weekID, dayOfWeek, newMealsString);
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        AlertDialog delScreen = builder.create();
        delScreen.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.recipes:
              startActivity(new Intent(this, MainActivity.class));
              return true;

            case R.id.calendar:
                //startActivity(new Intent(this, CalendarActivity.class));
                return true;

            case R.id.shoppingList:
                startActivity(new Intent(this, shoppingListActivity.class));
                return true;
        }
        return false;
    }

}