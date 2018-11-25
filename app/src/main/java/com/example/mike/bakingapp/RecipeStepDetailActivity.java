package com.example.mike.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mike.bakingapp.adapter.StepFragmentPagerAdapter;
import com.example.mike.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single RecipeStep detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeInfoActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {
    @BindView(R.id.recipe_step_tab_layout)
    TabLayout mRecipeStepTl;
    @BindView(R.id.recipe_step_viewpager)
    ViewPager mRecipeStepVp;

    private Recipe mRecipe;
    private int mStepSelected;

    public static final String RECIPE = "recipe";
    public static final String STEP_SELECTED = "step_selected";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null&&bundle.containsKey(RECIPE)&&bundle.containsKey(STEP_SELECTED)){
            mRecipe = bundle.getParcelable(RECIPE);
            mStepSelected=bundle.getInt(STEP_SELECTED);
        } else {
            Toast.makeText(this,R.string.no_step_data,Toast.LENGTH_LONG);
            finish();
        }
        StepFragmentPagerAdapter adapter=new StepFragmentPagerAdapter(getApplicationContext(),mRecipe.getSteps(),getSupportFragmentManager());
        mRecipeStepVp.setAdapter(adapter);
        mRecipeStepTl.setupWithViewPager(mRecipeStepVp);
        mRecipeStepVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setTitle(mRecipe.getSteps().get(i).getShortDescription());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mRecipeStepVp.setCurrentItem(mStepSelected);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
