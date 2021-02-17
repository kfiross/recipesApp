package com.recipesapp.recipesapp.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Recipe;
import com.recipesapp.recipesapp.databinding.FragmentSearchBinding;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.utils.TextChangedListener;
import com.recipesapp.recipesapp.views.adapters.RecipeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
            mBinding.btnSearch.setEnabled(!mTags.isEmpty());
        });
    }

    private void initBinding(){
        RecipeAdapter categoryAdapter = new RecipeAdapter(new ArrayList<>());
        mBinding.recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recipesRecyclerView.setHasFixedSize(true);
        mBinding.recipesRecyclerView.setAdapter(categoryAdapter);

        mBinding.setTags(mTags);
        mBinding.setCanSearch(false);
        mBinding.setIsLoading(null);

//        mBinding.setOnTextChanged((s, start, before, count) -> {
//            mBinding.setCanSearch(!s.toString().isEmpty());
//        });

        mBinding.etSearchTag.addTextChangedListener(new TextChangedListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                mBinding.setCanSearch(!aNew.isEmpty());
            }
        });


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
        mBinding.setIsLoading(true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            final ArrayList<Recipe> recipes = new ArrayList<>();

            //Background work here
            try {
                recipes.addAll(FirestoreUtils.searchRecipes(mTags));
            } catch (ExecutionException e) {
                e.printStackTrace();
                mBinding.setIsLoading(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
                mBinding.setIsLoading(false);
            }

            handler.post(() -> {
                //UI Thread work here
                ((RecipeAdapter)mBinding.recipesRecyclerView.getAdapter()).setItems(recipes);
                mBinding.setIsLoading(false);
            });
        });
    }
}