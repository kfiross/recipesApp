package com.foodiz.app.views.fragments;

import android.os.Bundle;
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
import com.foodiz.app.views.adapters.RecipeAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

        mRecyclerView = mBinding.recyclerViewRecipes;

        mBinding.setName(mSelectedCategoryName);


        mBinding.appbar.btnSearch.setVisibility(View.VISIBLE);
        mBinding.appbar.btnSearch.setOnClickListener(v -> {
            mBinding.appbar.toolbar.setVisibility(View.GONE);

            // locate "search button" to automatically start to type for search
            int searchButtonId = mBinding.searchBar.getContext().getResources()
                    .getIdentifier("android:id/search_button", null, null);
            ImageView closeButton = mBinding.searchBar.findViewById(searchButtonId);
            closeButton.callOnClick();
        });

        mBinding.searchBar.setOnCloseListener(() -> {
            mBinding.appbar.toolbar.setVisibility(View.VISIBLE);
            setupRecyclerView(mCategoryRecipes);
            return true;
        });

        setupRecyclerView(new ArrayList<>());

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

        fetchDocs();
    }

    private void fetchDocs(){
        FirebaseFirestore.getInstance().collection("recipes")
                .whereEqualTo("category", mSelectedCategoryId)
                .addSnapshotListener(
                (documentSnapshots, error) -> {

                    List<DocumentSnapshot> docs = documentSnapshots.getDocuments();
                    mCategoryRecipes = new ArrayList<>();
                    for (int i = 0; i < docs.size(); i++) {
                        DocumentSnapshot documentSnapshot = docs.get(i);
                        // documentSnapshot.toObject(Recipe.class);

                        Recipe newRecipe = Recipe.fromDocument(documentSnapshot);

                        mCategoryRecipes.add(newRecipe);
                    }
                    setupRecyclerView(mCategoryRecipes);

                });
    }

    private void setupRecyclerView(ArrayList<Recipe> recipes){
        RecipeAdapter categoryAdapter = new RecipeAdapter(recipes);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}