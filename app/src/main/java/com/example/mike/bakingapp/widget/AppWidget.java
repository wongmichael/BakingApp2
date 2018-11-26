package com.example.mike.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.mike.bakingapp.MainActivity;
import com.example.mike.bakingapp.Prefs;
import com.example.mike.bakingapp.R;
import com.example.mike.bakingapp.model.Recipe;

public class AppWidget extends AppWidgetProvider {
    public static void updateAppWidget(Context c,AppWidgetManager appWidgetManager,int appWidgetId){
        Recipe recipe = Prefs.loadRecipe(c);
        if(recipe!=null){
            PendingIntent pendingIntent=PendingIntent.getActivity(c,0,new Intent(c,MainActivity.class),0);
            RemoteViews remoteViews = new RemoteViews(c.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.widget_label,recipe.getName());
            remoteViews.setOnClickPendingIntent(R.id.widget_label,pendingIntent);

            Intent intent=new Intent(c,AppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            remoteViews.setRemoteAdapter(R.id.widget_lv,intent);
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_lv);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int id : appWidgetIds){
            updateAppWidget(context,appWidgetManager,id);
        }
    }
    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for(int id:appWidgetIds){
            updateAppWidget(context,appWidgetManager,id);
        }
    }

}
