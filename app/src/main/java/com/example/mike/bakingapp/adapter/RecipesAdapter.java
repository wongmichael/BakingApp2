package com.example.mike.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mike.bakingapp.R;
import com.example.mike.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>{

    private OnItemClickListener mOnItemClickListener;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list_item,viewGroup,false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, final int i) {
        recipeViewHolder.mTvRecipeName.setText(mRecipes.get(i).getName());
        recipeViewHolder.mTvRecipeServings.setText(mContext.getString(R.string.servings));
        recipeViewHolder.mTvRecipeServings.append(" "+String.valueOf(mRecipes.get(i).getServings()));
        //recipeViewHolder.mTvRecipeServings.setText(mContext.getString(R.string.servings,mRecipes.get(i).getServings()));
        String recipeImage = mRecipes.get(i).getImage();
        if(!recipeImage.isEmpty()) {
            Picasso.with(mContext)
                    .load(recipeImage)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(recipeViewHolder.mIvRecipeImage);
        }

        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null) {
                    mOnItemClickListener.onItemClick(i);
                }
            }
        });
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
