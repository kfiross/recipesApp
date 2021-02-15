package com.recipesapp.recipesapp.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Recipe;
import com.recipesapp.recipesapp.databinding.FragmentSearchBinding;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.views.adapters.RecipeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {
    private FragmentSearchBinding mBinding;

    private ArrayList<String> mTags;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTags = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_search, container, false);
        // inflate this view
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBinding();
        setButtonsClickListeners();

        mBinding.chipGroupIngredients.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            updateClickListeners();
        });
    }

    private void initBinding(){
        RecipeAdapter categoryAdapter = new RecipeAdapter(new ArrayList<>());
        mBinding.recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recipesRecyclerView.setHasFixedSize(true);
        mBinding.recipesRecyclerView.setAdapter(categoryAdapter);

        mBinding.setTags(mTags);
    }

    private void setButtonsClickListeners(){
        mBinding.btnAddTag.setOnClickListener(v -> {
            mTags.add(mBinding.etSearchTag.getText().toString());
            mBinding.setTags(mTags);
            mBinding.etSearchTag.setText("");
        });

        mBinding.btnSearch.setOnClickListener(v -> searchResult());
    }

    private void updateClickListeners() {
        for(int i=0; i<mTags.size(); i++){
            int finalI = i;
            mBinding.chipGroupIngredients.getChildAt(i).setOnClickListener(v -> {
                mTags.remove(finalI);
                mBinding.setTags(mTags);
            });
        }
    }

    private void searchResult(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            final ArrayList<Recipe> recipes = new ArrayList<>();

            //Background work here
            try {
                recipes.addAll(FirestoreUtils.searchRecipes(mTags));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                //UI Thread work here
                ((RecipeAdapter)mBinding.recipesRecyclerView.getAdapter()).setItems(recipes);
            });
        });
    }
}