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
import com.example.mike.bakingapp.model.Ingredient;
import com.example.mike.bakingapp.model.Recipe;
import com.example.mike.bakingapp.model.Step;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OnItemClickListener mOnItemClickListener;
    private Recipe mRecipe;

    public RecipeAdapter(Recipe r, OnItemClickListener l){
        this.mRecipe = r;
        this.mOnItemClickListener=l;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Logger.d(viewType);
        if(viewType==0){
            return new IngredientViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_ingredient_list_item,viewGroup,false));
        } else {
            return new StepViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_step_list_item,viewGroup,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof IngredientViewHolder){
            IngredientViewHolder viewHolder = (IngredientViewHolder) holder;
            List<Ingredient> ingredients = mRecipe.getIngredients();
            int ingredientsSize = ingredients.size();
            StringBuilder ingredientsText = new StringBuilder();
            for(int i=0;i<ingredientsSize;i++){
                Ingredient ing = ingredients.get(i);
                ingredientsText.append(String.format(Locale.getDefault(),"- %.1f %s %s",ing.getQuantity(),ing.getMeasure(),ing.getIngredient()));
                if(i!=ingredientsSize-1) ingredientsText.append("\n");
            }
            viewHolder.mTvIngredients.setText(ingredientsText);
        } else if(holder instanceof StepViewHolder){
            StepViewHolder viewHolder = (StepViewHolder) holder;
            viewHolder.mTvStepOrder.setText(position+"-");
            List<Step> steps = mRecipe.getSteps();
            viewHolder.mTvStepName.setText(steps.get(position).getShortDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener!=null) mOnItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0) return 0;
        else return 99;
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredients) public TextView mTvIngredients;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.step_name) public TextView mTvStepName;
        @BindView(R.id.step_order) public TextView mTvStepOrder;
        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
