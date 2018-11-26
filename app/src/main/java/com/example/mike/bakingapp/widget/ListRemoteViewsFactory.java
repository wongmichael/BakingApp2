package com.example.mike.bakingapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.mike.bakingapp.Prefs;
import com.example.mike.bakingapp.R;
import com.example.mike.bakingapp.model.Recipe;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Recipe recipe;
    public ListRemoteViewsFactory(Context c){this.mContext=c;}
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe= Prefs.loadRecipe(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        row.setTextViewText(R.id.tv_widget_ingredient_item,recipe.getIngredients().get(position).getIngredient());
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;//0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
