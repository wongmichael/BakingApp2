package com.example.mike.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mike.bakingapp.model.Recipe;
import com.google.gson.Gson;

import java.util.logging.Logger;

public class Prefs {
    public static final String PREFS = "prefs";
    public static void saveRecipe(Context c, Recipe r){
        SharedPreferences.Editor prefsEditor = c.getSharedPreferences(PREFS,Context.MODE_PRIVATE).edit();
        //com.orhanobut.logger.Logger.d(new Gson().toJson(r));
        prefsEditor.putString(c.getString(R.string.widget_recipe_key),new Gson().toJson(r));
        prefsEditor.apply();
    }
    public static Recipe loadRecipe(Context c){
        SharedPreferences prefs = c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        String recipeJson=prefs.getString(c.getString(R.string.widget_recipe_key),"");

        if("".equals(recipeJson)) {
            return null;
        } else {
            //com.orhanobut.logger.Logger.d(new Gson().fromJson(recipeJson,Recipe.class));
            return new Gson().fromJson(recipeJson,Recipe.class);
        }
    }
}
