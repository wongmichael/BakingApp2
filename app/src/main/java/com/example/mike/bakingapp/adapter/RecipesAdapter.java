package com.example.mike.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mike.bakingapp.R;
import com.example.mike.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.BitSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>{

    private final OnItemClickListener mOnItemClickListener;
    private List<Recipe> mRecipes;
    private Context mContext;


    public RecipesAdapter(Context c, List<Recipe> r, OnItemClickListener l){
        this.mContext = c;
        this.mRecipes = r;
        this.mOnItemClickListener=l;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_recipe,viewGroup,false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.mTvRecipeName.setText(mRecipes.get(i).getName());
        recipeViewHolder.mTvRecipeServings.setText(R.string.servings+" "+mRecipes.get(i).getServings());
        Picasso.with(mContext)
                .load(mRecipes.get(i).getImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(recipeViewHolder.mIvRecipeImage);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_name) public TextView mTvRecipeName;
        @BindView(R.id.recipe_servings) public TextView mTvRecipeServings;
        @BindView(R.id.recipe_image) public ImageView mIvRecipeImage;
        public RecipeViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
