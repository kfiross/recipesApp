package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodiz.app.MainActivity;
import com.foodiz.app.R;
import com.foodiz.app.databinding.FragmentRecipesBinding;
import com.foodiz.app.model.Recipe;
import com.foodiz.app.helper.FirestoreUtils;
import com.foodiz.app.views.adapters.RecipeAdapter;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment {

    private FragmentRecipesBinding mBinding;
    private RecyclerView mRecyclerView;
    private int mSelectedCategoryId;
    private String mSelectedCategoryName;

    private ArrayList<Recipe> mCategoryRecipes;

    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            mSelectedCategoryId = args.getInt("category_id", -1);
            mSelectedCategoryName = args.getString("category_name", "");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipes, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).setSupportActionBar(mBinding.appbar.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        MainActivity.preferencesConfig.loadLocal(getContext());


        mRecyclerView = mBinding.recyclerViewRecipes;

        mBinding.setName(mSelectedCategoryName);

        setupRecyclerView(new ArrayList<>());
        setupSearchBar();

        fetchDocs();
    }

    private void setupSearchBar() {
        // setup binding for show/hiding search bar
        mBinding.appbar.btnSearch.setVisibility(View.VISIBLE);
        mBinding.appbar.btnSearch.setOnClickListener(v -> {
            mBinding.appbar.toolbar.setVisibility(View.GONE);

            // locate "search button" to automatically start to type for search
            int searchButtonId = mBinding.searchBar.getContext().getResources()
                    .getIdentifier("android:id/search_button", null, null);
            ImageView closeButton = mBinding.searchBar.findViewById(searchButtonId);
            closeButton.callOnClick();
        });

        // define close action call
        mBinding.searchBar.setOnCloseListener(() -> {
            mBinding.appbar.toolbar.setVisibility(View.VISIBLE);
            setupRecyclerView(mCategoryRecipes);
            return true;
        });

        // listener when query text changed
        mBinding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Recipe> resultsRecipes = new ArrayList<>();
                for(Recipe recipe : mCategoryRecipes){
                    // case query is part of a recipe's name
                    if(recipe.getName().contains(newText)){
                        resultsRecipes.add(recipe);
                    }

                    // case query is an ingredient name that a recipe has
                    else if(recipe.containsIngredient(newText)){
                        resultsRecipes.add(recipe);
                    }
                }

                setupRecyclerView(resultsRecipes);
                return true;
            }
        });
    }

    private void fetchDocs(){
        final ArrayList<String> myRecipesIds = new ArrayList<>(MainActivity.preferencesConfig.readMyRecipesIds());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            //Background work here
            mCategoryRecipes = new ArrayList<>();
            for (String recipeId : myRecipesIds) {
                try {
                    DocumentSnapshot documentSnapshot = FirestoreUtils.getRecipe(recipeId);
                    Recipe newRecipe = Recipe.fromDocument(documentSnapshot);

                    // only add my recipes that are on the current category
                    if(newRecipe.getCategory() == mSelectedCategoryId){
                        mCategoryRecipes.add(newRecipe);
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            handler.post(() -> {
                //UI Thread work here
                setupRecyclerView(mCategoryRecipes);
            });
        });
    }

    private void setupRecyclerView(ArrayList<Recipe> recipes){
        RecipeAdapter categoryAdapter = new RecipeAdapter(recipes);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}