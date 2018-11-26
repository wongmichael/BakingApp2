package com.example.mike.bakingapp;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

import com.orhanobut.logger.AndroidLogAdapter;

import java.util.logging.Logger;

public class GlobalApplication extends Application {
    //null in prod
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * only for testing
     */
    @VisibleForTesting
    @NonNull
    private IdlingResource initializeIdlingResource(){
        if(mIdlingResource==null) mIdlingResource=new SimpleIdlingResource();
        return mIdlingResource;
    }

    public GlobalApplication(){
        if(BuildConfig.DEBUG) initializeIdlingResource(); //idlingresource null in prod

        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG; //loggable only on debug
            }
        });
    }

    public void setIdleState(boolean state){
        if(mIdlingResource!=null) mIdlingResource.setIdleState(state);
    }

    @Nullable
    public SimpleIdlingResource getmIdlingResource(){
        return mIdlingResource;
    }
}
