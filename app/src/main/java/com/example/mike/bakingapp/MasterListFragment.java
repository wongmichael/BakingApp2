package com.example.mike.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mike.bakingapp.adapter.RecipesAdapter;
import com.example.mike.bakingapp.api.RecipesApiCallback;
import com.example.mike.bakingapp.api.RecipesApiManager;
import com.example.mike.bakingapp.model.Recipe;

import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment {
    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecipesRv;

    private static String KEY_RECIPES = "recipes";

    private List<Recipe> mRecipes;
    OnListItemClickListener mCallback;
    private GlobalApplication globalApplication;
    private static String RECIPE_KEY = "recipe";

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListItemClickListener {
        void onRecipeSelected(Recipe r);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnListItemClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement OnListItemClickListener");
        }
    }

    public MasterListFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_master_list,container,false);
        //mRecipesRv = fragView.findViewById(R.id.rv_recipe_list);
        ButterKnife.bind(this,fragView); //mRecipesRv
        mRecipesRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        mRecipesRv.setLayoutManager(layoutManager);

        globalApplication = (GlobalApplication) getActivity().getApplicationContext();
        globalApplication.setIdleState(false);

        if(savedInstanceState!=null && savedInstanceState.containsKey(RECIPE_KEY)){
            mRecipes=savedInstanceState.getParcelableArrayList(RECIPE_KEY);
            mRecipesRv.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), mRecipes, new RecipesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mCallback.onRecipeSelected(mRecipes.get(position));
                }
            }));
            globalApplication.setIdleState(true);
        }

        return fragView;
    }

    private void getRecipes(){
        RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
            @Override
            public void onResponse(List<Recipe> result) {
                //com.orhanobut.logger.Logger.e("onResponse");
                if(result!=null){
                    com.orhanobut.logger.Logger.d(result);
                    mRecipes = result;
                    RecipesAdapter.OnItemClickListener listener = new RecipesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            mCallback.onRecipeSelected(mRecipes.get(position));
                        }
                    };
                    RecipesAdapter mRecipesAdapter = new RecipesAdapter(getActivity().getApplicationContext(),mRecipes,listener);
                    mRecipesRv.setAdapter(mRecipesAdapter);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),getString(R.string.no_recipe_data),Toast.LENGTH_LONG);
                    com.orhanobut.logger.Logger.d("no result");
                }
                globalApplication.setIdleState(true);
            }

            @Override
            public void onCancel() {
                globalApplication.setIdleState(true);
                com.orhanobut.logger.Logger.e("onCancel");
            }
        });
    }

    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(mRecipes==null){
                getRecipes();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(networkChangeReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(networkChangeReceiver);
    }

}
