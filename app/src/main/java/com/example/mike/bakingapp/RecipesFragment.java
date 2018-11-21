package com.example.mike.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.bakingapp.api.RecipesApiCallback;
import com.example.mike.bakingapp.api.RecipesApiManager;
import com.example.mike.bakingapp.model.DummyContent;
import com.example.mike.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;

public class RecipesFragment extends Fragment {
    @BindView(R.id.item_list)
    RecyclerView mRecipesRecyclerView;

    private List<Recipe> mRecipes;
    private boolean mTwoPane;

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getActivity().getApplicationContext(),
                mRecipes,//DummyContent.ITEMS,
                mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        //private final ItemListActivity mParentActivity;
        //private final List<DummyContent.DummyItem> mValues;
        private final List<Recipe> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    //mParentActivity.
                            getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };
        private Context mContext;

        SimpleItemRecyclerViewAdapter(//ItemListActivity parent,
                                      Context context,
                                      List<Recipe> items,//List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            //mParentActivity = parent;
            this.mContext = context;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getId()); //id);
            holder.mContentView.setText(mValues.get(position).getName()); //content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRecipes==null) loadRecipes();
        }
    };

    private void loadRecipes() {
        RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
            @Override
            public void onResponse(List<Recipe> result) {
                if(result!= null){
                    mRecipes=result;
                    //mRecipesRecyclerView.setAdapter();
                    assert mRecipesRecyclerView != null;
                    setupRecyclerView(mRecipesRecyclerView);

                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public RecipesFragment(){
        //empty pub constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }
}
