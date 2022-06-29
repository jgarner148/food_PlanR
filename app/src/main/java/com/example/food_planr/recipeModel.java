package com.example.food_planr;

public class recipeModel {
    String title;
    String ingredients;


    public String getTitle() {
        return title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public recipeModel(String title, String ingredients) {
        this.title = title;
        this.ingredients = ingredients;
    }
}
