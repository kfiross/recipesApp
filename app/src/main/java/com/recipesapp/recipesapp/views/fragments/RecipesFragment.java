package com.recipesapp.recipesapp.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recipesapp.recipesapp.MainActivity;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.databinding.FragmentRecipesBinding;
import com.recipesapp.recipesapp.model.Recipe;
import com.recipesapp.recipesapp.views.adapters.RecipeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends BaseFragment {

    private FragmentRecipesBinding mBinding;
    private RecyclerView mRecyclerView;
    private int mSelectedCategoryId;
    private String mSelectedCategoryName;

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

        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        mRecyclerView = mBinding.recyclerViewRecipes;

        mBinding.setName(mSelectedCategoryName);

        setupRecyclerView(new ArrayList<>());

        fetchDocs();
    }

    private void fetchDocs(){
        FirebaseFirestore.getInstance().collection("recipes")
                .whereEqualTo("category", mSelectedCategoryId)
                .addSnapshotListener(
                (documentSnapshots, error) -> {

                    List<DocumentSnapshot> docs = documentSnapshots.getDocuments();
                    ArrayList<Recipe> recipes = new ArrayList<>();
                    for (int i = 0; i < docs.size(); i++) {
                        DocumentSnapshot documentSnapshot = docs.get(i);
                        // documentSnapshot.toObject(Recipe.class);

                        Recipe newRecipe = Recipe.fromDocument(documentSnapshot);

                        recipes.add(newRecipe);
                    }
                    setupRecyclerView(recipes);

                });
    }

    private void setupRecyclerView(ArrayList<Recipe> recipes){
        RecipeAdapter categoryAdapter = new RecipeAdapter(recipes);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}