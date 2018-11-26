package com.example.mike.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mike.bakingapp.model.Recipe;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity implements MasterListFragment.OnListItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeSelected(Recipe r) {
        Intent intent = new Intent(this,RecipeInfoActivity.class);
        intent.putExtra(RecipeInfoActivity.RECIPE_KEY, r);
        startActivity(intent);
    }
}
