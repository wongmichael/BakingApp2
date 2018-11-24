package com.example.mike.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.mike.bakingapp.model.Recipe;

import butterknife.BindView;

public class RecipeInfoActivity extends AppCompatActivity {
    public static final String RECIPE_KEY = "recipe";
    @BindView(R.id.rv_recipe_steps)
    RecyclerView mStepsRv;

    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
