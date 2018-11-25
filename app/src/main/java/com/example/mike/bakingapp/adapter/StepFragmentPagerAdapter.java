package com.example.mike.bakingapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mike.bakingapp.RecipeStepDetailFragment;
import com.example.mike.bakingapp.model.Step;

import java.util.List;

public class StepFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Step> mSteps;

    public StepFragmentPagerAdapter(Context c, List<Step> s, FragmentManager fm) {
        super(fm);
        this.mContext=c;
        this.mSteps=s;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeStepDetailFragment.STEP,mSteps.get(position));
        RecipeStepDetailFragment f = new RecipeStepDetailFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format("Step%d",position+1);
    }
}
