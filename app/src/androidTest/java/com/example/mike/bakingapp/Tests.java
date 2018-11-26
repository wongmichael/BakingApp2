package com.example.mike.bakingapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.View;
import android.widget.FrameLayout;

import com.example.mike.bakingapp.model.Recipe;
import com.orhanobut.logger.Logger;

@RunWith(AndroidJUnit4.class)
public class Tests {
    GlobalApplication globalApplication;
    IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        globalApplication = (GlobalApplication) mainActivityActivityTestRule.getActivity().getApplicationContext();
        mIdlingResource = globalApplication.getmIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void unregisterIdlingResource(){
        if(mIdlingResource!=null){
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void clickRecyclerViewItemHasIntentWithKey(){
        Intents.init();
        getMeToRecipeInfo(0);
        Intents.intended(IntentMatchers.hasExtraWithKey(RecipeInfoActivity.RECIPE_KEY));
        Intents.release();
    }

    @Test
    public void clickOnRecyclerViewItem_opensRecipeInfoActivity(){
        getMeToRecipeInfo(0);
        onView(withId(R.id.tv_ingredients))
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.rv_recipe_steps))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void clickOnRecyclerViewStepItem_opensRecipeStepActivity_orFragment(){
        getMeToRecipeInfo(0);
/*        View v = mainActivityActivityTestRule.getActivity().findViewById(R.id.recipe_step_detail_container);
        Logger.d(v);*/
        boolean twoPane = globalApplication.getResources().getBoolean(R.bool.twoPane);
        Logger.d(twoPane);
        if(!twoPane){
        //if(onView(withId(R.id.recipe_step_detail_container)).equals(null)){
            //singlePane
            Intents.init();
            selectRecipeStep(1);
            Intents.intended(IntentMatchers.hasComponent(RecipeStepDetailActivity.class.getName()));
            Intents.intended(IntentMatchers.hasExtraWithKey(RecipeStepDetailActivity.RECIPE));
            Intents.intended(IntentMatchers.hasExtraWithKey(RecipeStepDetailActivity.STEP_SELECTED));
            Intents.release();

            onView(withId(R.id.recipe_step_tab_layout))
                    .check(ViewAssertions.matches(isCompletelyDisplayed()));
        }else {
            //twoPaneMode
            selectRecipeStep(1);
            onView(withId(R.id.exo_player_view))
                    .check(ViewAssertions.matches(isDisplayed()));
        }
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void checkAddWidgetButtonFunctionality(){
        globalApplication.getSharedPreferences(Prefs.PREFS,Context.MODE_PRIVATE).edit()
                .clear()
                .commit();
        getMeToRecipeInfo(0);
        onView(withId(R.id.action_add_to_widget))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Recipe recipe = Prefs.loadRecipe(globalApplication);
        Assert.assertNotNull(recipe);
    }

    @Ignore
    public static void getMeToRecipeInfo(int recipePosition){
        //onView(withId(R.id.rv_recipe_list)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipePosition,click()));
    }

    @Ignore
    public static void selectRecipeStep(int recipeStep){
        onView(withId(R.id.rv_recipe_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipeStep,click()));
    }
}
