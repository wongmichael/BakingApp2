package com.example.mike.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.bakingapp.adapter.RecipesAdapter;
import com.example.mike.bakingapp.api.RecipesApiCallback;
import com.example.mike.bakingapp.api.RecipesApiManager;
import com.example.mike.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;

public class MasterListFragment extends Fragment {
    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecipesRv;

    private static String KEY_RECIPES = "recipes";

    private List<Recipe> mRecipes;

    public MasterListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_master_list,container,false);
    }

    private void getRecipes(){
        RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
            @Override
            public void onResponse(List<Recipe> result) {
                if(result!=null){
                    mRecipes = result;
                    RecipesAdapter mRecipesAdapter = new RecipesAdapter(getActivity().getApplicationContext(),mRecipes,null);
                    mRecipesRv.setAdapter(mRecipesAdapter);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
