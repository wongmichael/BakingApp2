package com.example.mike.bakingapp.api;

public interface RecipesApiCallback<T> {
    void onResponse(T result);
    void onCancel();
}
