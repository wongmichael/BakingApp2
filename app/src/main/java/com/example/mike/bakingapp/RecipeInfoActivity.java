package com.example.mike.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.mike.bakingapp.adapter.RecipeAdapter;
import com.example.mike.bakingapp.model.Recipe;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeInfoActivity extends AppCompatActivity {
    public static final String RECIPE_KEY = "recipe";
    @BindView(R.id.rv_recipe_steps)
    RecyclerView mStepsRv;

    @Nullable @BindView(R.id.recipe_step_detail_container)
    FrameLayout mRecipeStepDetailContainer;

    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null&&bundle.containsKey(RECIPE_KEY)){
            mRecipe=bundle.getParcelable(RECIPE_KEY);
        } else {
            Toast.makeText(this,R.string.no_recipe_data,Toast.LENGTH_LONG);
            finish();
        }
        setContentView(R.layout.activity_recipe_info);
        ButterKnife.bind(this);
        setTitle(mRecipe.getName());
        if(mRecipeStepDetailContainer!=null){
            mTwoPane=true;
            if(savedInstanceState==null && !mRecipe.getSteps().isEmpty()){
                showStep(0);
            }
        }
        Logger.d(mTwoPane);
        mStepsRv.setAdapter(new RecipeAdapter(mRecipe, new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showStep(position);
            }
        }));
    }

    private void showStep(int position) {
        if(mTwoPane){
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeStepDetailFragment.STEP,mRecipe.getSteps().get(position));
            RecipeStepDetailFragment f=new RecipeStepDetailFragment();
            f.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container,f)
                    .commit();
        } else{
            Intent intent = new Intent(this,RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepDetailActivity.RECIPE,mRecipe);
            intent.putExtra(RecipeStepDetailActivity.STEP_SELECTED,position);
            startActivity(intent);
        }
    }
}
