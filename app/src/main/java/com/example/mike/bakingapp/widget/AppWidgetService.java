package com.example.mike.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.mike.bakingapp.Prefs;
import com.example.mike.bakingapp.model.Recipe;

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        return new ListRemoteViewsFactory(getApplicationContext());
    }

    public static void updateWidget(Context c, Recipe r){
        Prefs.saveRecipe(c,r);
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(c);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(c,AppWidget.class));
        AppWidget.updateAppWidgets(c,appWidgetManager,appWidgetIds);
    }
}
