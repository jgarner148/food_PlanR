package com.example.food_planr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
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

        if(calData.get("monRes").toString().equals("")){
            monMealText.setText("No meals Added");
        }
        else{
            monMealText.setText(calData.get("monRes").toString());
        }
        tuesMealText.setText(calData.get("tueRes").toString());
        wedMealText.setText(calData.get("wedRes").toString());
        thurTextMeal.setText(calData.get("thuRes").toString());
        friMealText.setText(calData.get("friRes").toString());
        satMealText.setText(calData.get("satRes").toString());
        sunMealText.setText(calData.get("sunRes").toString());

        //Setting up the delete buttons for each day
        Button mondayDelButton = (Button) findViewById(R.id.mondayDeleteButton);
        if(!monMealText.getText().toString().equals("")){
            mondayDelButton.setVisibility(View.VISIBLE);
        }
        mondayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(monMealText.getText().toString());
            }
        });

        Button tuesdayDelButton = (Button) findViewById(R.id.tuesdayDeleteButton);
        if(!tuesMealText.getText().toString().equals("")){
            tuesdayDelButton.setVisibility(View.VISIBLE);
        }
        tuesdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(tuesMealText.getText().toString());
            }
        });

        Button wednesdayDelButton = (Button) findViewById(R.id.wednesdayDeleteButton);
        if(!wedMealText.getText().toString().equals("")){
            wednesdayDelButton.setVisibility(View.VISIBLE);
        }
        wednesdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(wedMealText.getText().toString());
            }
        });

        Button thursdayDelButton = (Button) findViewById(R.id.thursdayDeleteButton);
        if(!thurTextMeal.getText().toString().equals("")){
            thursdayDelButton.setVisibility(View.VISIBLE);
        }
        thursdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(thurTextMeal.getText().toString());
            }
        });

        Button fridayDelButton = (Button) findViewById(R.id.fridayDeleteButton);
        if(!friMealText.getText().toString().equals("")){
            fridayDelButton.setVisibility(View.VISIBLE);
        }
        fridayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(friMealText.getText().toString());
            }
        });

        Button saturdayDelButton = (Button) findViewById(R.id.saturdayDeleteButton);
        if(!satMealText.getText().toString().equals("")){
            saturdayDelButton.setVisibility(View.VISIBLE);
        }
        saturdayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(satMealText.getText().toString());
            }
        });

        Button sundayDelButton = (Button) findViewById(R.id.sundayDeleteButton);
        if(!sunMealText.getText().toString().equals("")){
            sundayDelButton.setVisibility(View.VISIBLE);
        }
        sundayDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDelOptions(sunMealText.getText().toString());
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
                            db.addMealToCal(currentWeek,monMealText.getText().toString(), "MONDAY", menuItem.getTitle().toString());
                            if(monMealText.getText().toString().equals("No meals Added")){
                                monMealText.setText(menuItem.getTitle().toString());
                            }
                            else{
                                monMealText.setText(monMealText.getText().toString() + "\n" + menuItem.getTitle().toString());
                            }
                            mondayDelButton.setVisibility(View.VISIBLE);
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
                            db.addMealToCal(currentWeek,tuesMealText.getText().toString(), "TUESDAY", menuItem.getTitle().toString());
                            if(tuesMealText.getText().toString().equals("")){
                                tuesMealText.setText(menuItem.getTitle().toString());
                            }
                            else{
                                tuesMealText.setText(tuesMealText.getText().toString() + "\n" + menuItem.getTitle().toString());
                            }
                            tuesdayDelButton.setVisibility(View.VISIBLE);
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
                            db.addMealToCal(currentWeek,wedMealText.getText().toString(), "WEDNESDAY", menuItem.getTitle().toString());
                            if(wedMealText.getText().toString().equals("")){
                                wedMealText.setText(menuItem.getTitle().toString());
                            }
                            else{
                                wedMealText.setText(wedMealText.getText().toString() + "\n" + menuItem.getTitle().toString());
                            }
                            wednesdayDelButton.setVisibility(View.VISIBLE);
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
                            db.addMealToCal(currentWeek,thurTextMeal.getText().toString(), "THURSDAY", menuItem.getTitle().toString());
                            if(thurTextMeal.getText().toString().equals("")){
                                thurTextMeal.setText(menuItem.getTitle().toString());
                            }
                            else{
                                thurTextMeal.setText(thurTextMeal.getText().toString() + "\n" + menuItem.getTitle().toString());
                            }
                            thursdayDelButton.setVisibility(View.VISIBLE);
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
                            db.addMealToCal(currentWeek,friMealText.getText().toString(), "FRIDAY", menuItem.getTitle().toString());
                            if(friMealText.getText().toString().equals("")){
                                friMealText.setText(menuItem.getTitle().toString());
                            }
                            else{
                                friMealText.setText(friMealText.getText().toString() + "\n" + menuItem.getTitle().toString());
                            }
                            fridayDelButton.setVisibility(View.VISIBLE);
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
                            db.addMealToCal(currentWeek,satMealText.getText().toString(), "SATURDAY", menuItem.getTitle().toString());
                            if(satMealText.getText().toString().equals("")){
                                satMealText.setText(menuItem.getTitle().toString());
                            }
                            else{
                                satMealText.setText(satMealText.getText().toString() + "\n" + menuItem.getTitle().toString());
                            }
                            saturdayDelButton.setVisibility(View.VISIBLE);
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
                            db.addMealToCal(currentWeek,sunMealText.getText().toString(), "SUNDAY", menuItem.getTitle().toString());
                            if(sunMealText.getText().toString().equals("")){
                                sunMealText.setText(menuItem.getTitle().toString());
                            }
                            else{
                                sunMealText.setText(sunMealText.getText().toString() + "\n" + menuItem.getTitle().toString());
                            }
                            sundayDelButton.setVisibility(View.VISIBLE);
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

    private void showDelOptions(String allRecipes){
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
                /**
                 * Method to remove item from array and database
                 * use String str = String.join(",", arr); to turn array into string
                 */
                Toast.makeText(CalendarActivity.this, "Deleted " + selection[0], Toast.LENGTH_SHORT).show();
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