package com.example.food_planr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String recipeTableName = "RECIPE_TABLE";
    public static final String COL_RECIPE_TITLE = "recipeTitle";
    public static final String COL_INGREDIENTS = "ingredients";

    public static final String mealCalTableName = "MEAL_CALENDAR_TABLE";
    public static final String COL_WEEK_ID = "weekId";
    public static final String COL_DATE_OF_MON = "dateOfMon";
    public static final String COL_RECIPE_TITLES_MON = "monRecipeTitles";
    public static final String COL_DATE_OF_TUE = "dateOfTue";
    public static final String COL_RECIPE_TITLES_TUE = "tueRecipeTitles";
    public static final String COL_DATE_OF_WED = "dateOfWed";
    public static final String COL_RECIPE_TITLES_WED = "wedRecipeTitles";
    public static final String COL_DATE_OF_THU = "dateOfThu";
    public static final String COL_RECIPE_TITLES_THU = "thuRecipeTitles";
    public static final String COL_DATE_OF_FRI = "dateOfFri";
    public static final String COL_RECIPE_TITLES_FRI = "friRecipeTitles";
    public static final String COL_DATE_OF_SAT = "dateOfSat";
    public static final String COL_RECIPE_TITLES_SAT = "satRecipeTitles";
    public static final String COL_DATE_OF_SUN = "dateOfSun";
    public static final String COL_RECIPE_TITLES_SUN = "sunRecipeTitles";

    public static final String userDataTableName = "USER_DATA_TABLE";
    public static final String COL_CURRENT_WEEK_ID = "currentWeekID";
    public static final String COL_MONDAY_DATE = "mondayDate";

    /**
     * Constructor for the database helper
     * @param context Current activity
     */
    public DataBaseHelper(@Nullable Context context) {
        super(context, "planRData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRecipeTableStatement = "CREATE TABLE " + recipeTableName + " (" + COL_RECIPE_TITLE + " STRING PRIMARY KEY, " +
                                                                                    COL_INGREDIENTS + " STRING)";

        String createMealCalTableStatement = "CREATE TABLE " + mealCalTableName + " (" + COL_WEEK_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                                                    COL_DATE_OF_MON + " STRING, " +
                                                                                    COL_RECIPE_TITLES_MON + " STRING, " +
                                                                                    COL_DATE_OF_TUE + " STRING, " +
                                                                                    COL_RECIPE_TITLES_TUE + " STRING, " +
                                                                                    COL_DATE_OF_WED + " STRING, " +
                                                                                    COL_RECIPE_TITLES_WED + " STRING, " +
                                                                                    COL_DATE_OF_THU + " STRING, " +
                                                                                    COL_RECIPE_TITLES_THU + " STRING, " +
                                                                                    COL_DATE_OF_FRI + " STRING, " +
                                                                                    COL_RECIPE_TITLES_FRI + " STRING, " +
                                                                                    COL_DATE_OF_SAT + " STRING, " +
                                                                                    COL_RECIPE_TITLES_SAT + " STRING, " +
                                                                                    COL_DATE_OF_SUN + " STRING, " +
                                                                                    COL_RECIPE_TITLES_SUN + " STRING)";

        String createUserDateTableStatement = "CREATE TABLE " + userDataTableName + " (" + COL_CURRENT_WEEK_ID + " STRING, " +
                COL_MONDAY_DATE + " STRING)";

        db.execSQL(createRecipeTableStatement);
        db.execSQL(createMealCalTableStatement);
        db.execSQL(createUserDateTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addRecipe(String title, String ingredients){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_RECIPE_TITLE, title);
        cv.put(COL_INGREDIENTS, ingredients);

        long insert = db.insert(recipeTableName,null,cv);
        db.close();
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getAllRecipes(){
        String query = "SELECT * FROM " + recipeTableName;
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query,null);
    }

    public boolean deleteRecipe(String recipeTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(recipeTableName, COL_RECIPE_TITLE + " = '" +  recipeTitle + "'", null)>0;
    }

    public boolean newWeek(String monDate, String tueDate, String wedDate, String thuDate, String friDate, String satDate, String sunDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String placeholder = "";

        cv.put(COL_DATE_OF_MON, monDate);
        cv.put(COL_RECIPE_TITLES_MON,placeholder);

        cv.put(COL_DATE_OF_TUE, tueDate);
        cv.put(COL_RECIPE_TITLES_TUE, placeholder);

        cv.put(COL_DATE_OF_WED, wedDate);
        cv.put(COL_RECIPE_TITLES_WED, placeholder);

        cv.put(COL_DATE_OF_THU, thuDate);
        cv.put(COL_RECIPE_TITLES_THU, placeholder);

        cv.put(COL_DATE_OF_FRI, friDate);
        cv.put(COL_RECIPE_TITLES_FRI, placeholder);

        cv.put(COL_DATE_OF_SAT, satDate);
        cv.put(COL_RECIPE_TITLES_SAT, placeholder);

        cv.put(COL_DATE_OF_SUN, sunDate);
        cv.put(COL_RECIPE_TITLES_SUN, placeholder);

        long insert = db.insert(mealCalTableName,null,cv);
        if(insert==-1){
            return false;
        }
        else{
            //Updates the user table with week ID and monday date
            String query1 = "SELECT " + COL_CURRENT_WEEK_ID + " FROM " + userDataTableName;
            Cursor cursor = db.rawQuery(query1,null);
            String result;
            int newWeekId = 0;
            if(cursor.moveToNext()){
                result = cursor.getString(0);
                newWeekId = Integer.parseInt(result) + 1;
                ContentValues cv2 = new ContentValues();
                cv2.put(COL_CURRENT_WEEK_ID, newWeekId);
                cv2.put(COL_MONDAY_DATE, monDate);

                String query2 = "UPDATE " + userDataTableName + " SET " + COL_CURRENT_WEEK_ID + " = " + newWeekId + ", " + COL_MONDAY_DATE + " = " + monDate;

                db.execSQL(query2);
                db.close();
                return true;
            }
            else{
                return false;
            }

        }
    }

    public boolean createUser(int mondayDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_CURRENT_WEEK_ID, "0");
        cv.put(COL_MONDAY_DATE, String.valueOf(mondayDate));

        long insert = db.insert(userDataTableName,null,cv);
        db.close();
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean checkUser(){
        String query = "SELECT * FROM " + userDataTableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            return true; //Returns true if it exists
        }
        else{
            return false; //Returns false if it doesn't exist
        }
    }

    public String[] getUser(){
        String query = "SELECT * FROM " + userDataTableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        String[] result = new String[7];
        if(cursor.moveToNext() && cursor.getCount()>=1){
            result[0] = cursor.getString(0);
            result[1]=cursor.getString(1);
        }
        return result;
    }

    public ContentValues getCalInfo(String currentweekID){
        String query = "SELECT * FROM " + mealCalTableName + " WHERE " + COL_WEEK_ID + " = " + currentweekID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ContentValues result = new ContentValues();
        if(cursor.moveToNext() && cursor.getCount()>=1){
            result.put("result", "success");
            result.put("monDate", cursor.getString(1));
            result.put("monRes", cursor.getString(2));
            result.put("tueDate", cursor.getString(3));
            result.put("tueRes", cursor.getString(4));
            result.put("wedDate", cursor.getString(5));
            result.put("wedRes", cursor.getString(6));
            result.put("thuDate", cursor.getString(7));
            result.put("thuRes", cursor.getString(8));
            result.put("friDate", cursor.getString(9));
            result.put("friRes", cursor.getString(10));
            result.put("satDate", cursor.getString(11));
            result.put("satRes", cursor.getString(12));
            result.put("sunDate", cursor.getString(13));
            result.put("sunRes", cursor.getString(14));
        }
        else{
            result.put("result", "fail");
        }
        return result;
    }

    public void addMealToCal(String weekID, String currentContent, String dayOfWeek, String textBeingAdded){
        //Getting the column title for the day of the week being added to
        String dayCol;
        if(dayOfWeek.equals("MONDAY")){
            dayCol = COL_RECIPE_TITLES_MON;
        }
        else if(dayOfWeek.equals("TUESDAY")){
            dayCol = COL_RECIPE_TITLES_TUE;
        }
        else if(dayOfWeek.equals("WEDNESDAY")){
            dayCol = COL_RECIPE_TITLES_WED;
        }
        else if(dayOfWeek.equals("THURSDAY")){
            dayCol = COL_RECIPE_TITLES_THU;
        }
        else if(dayOfWeek.equals("FRIDAY")){
            dayCol = COL_RECIPE_TITLES_FRI;
        }
        else if(dayOfWeek.equals("SATURDAY")){
            dayCol = COL_RECIPE_TITLES_SAT;
        }
        else{
            dayCol = COL_RECIPE_TITLES_SUN;
        }
        String query = "UPDATE " + mealCalTableName + " SET " + dayCol + " = '" + currentContent + "\n" + textBeingAdded + "' WHERE " + COL_WEEK_ID + " = " + weekID;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void updateMeals(String weekID, String dayOfWeek, String newContent){
        String dayCol;
        if(dayOfWeek.equals("monday")){
            dayCol = COL_RECIPE_TITLES_MON;
        }
        else if(dayOfWeek.equals("tuesday")){
            dayCol = COL_RECIPE_TITLES_TUE;
        }
        else if(dayOfWeek.equals("wednesday")){
            dayCol = COL_RECIPE_TITLES_WED;
        }
        else if(dayOfWeek.equals("thursday")){
            dayCol = COL_RECIPE_TITLES_THU;
        }
        else if(dayOfWeek.equals("friday")){
            dayCol = COL_RECIPE_TITLES_FRI;
        }
        else if(dayOfWeek.equals("saturday")){
            dayCol = COL_RECIPE_TITLES_SAT;
        }
        else{
            dayCol = COL_RECIPE_TITLES_SUN;
        }

        String query = "UPDATE " + mealCalTableName + " SET " + dayCol + " = '" + newContent + "' WHERE " + COL_WEEK_ID + " = " + weekID;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
}
