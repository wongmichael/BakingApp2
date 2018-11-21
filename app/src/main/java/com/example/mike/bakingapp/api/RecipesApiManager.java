package com.example.mike.bakingapp.api;

import com.example.mike.bakingapp.model.Recipe;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesApiManager implements Serializable {
    private static volatile RecipesApiManager sharedInstance = new RecipesApiManager();
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private RecipesApiService recipesApiService;

    private RecipesApiManager(){
        if (sharedInstance!=null){
            throw new RuntimeException("Use getInstance() for the single instance of this class"); //singleton
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        recipesApiService = retrofit.create(RecipesApiService.class);
    }

    public static RecipesApiManager getInstance(){
        if(sharedInstance == null){
            synchronized (RecipesApiManager.class){
                if(sharedInstance==null) sharedInstance = new RecipesApiManager();
            }
        }
        return sharedInstance;
    }

    public void getRecipes(final RecipesApiCallback<List<Recipe>> recipesApiCallback){
        recipesApiService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipesApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                if (call.isCanceled()){
                    Logger.e("Request cancelled.");
                    recipesApiCallback.onCancel();
                } else {
                    Logger.e(t.getMessage());
                    recipesApiCallback.onResponse(null);
                }
            }
        });
    }

}
