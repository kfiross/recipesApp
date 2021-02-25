package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodiz.app.R;
import com.foodiz.app.databinding.FragmentHomeBinding;
import com.foodiz.app.model.Category;
import com.foodiz.app.views.adapters.CategoryAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FragmentHomeBinding mBinding;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController =
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        mBinding.btnGotoMyRecipes.setOnClickListener(v -> {
            navController.navigate(R.id.myRecipesFragment);
        });

        mBinding.btnGotoNewRecipe.setOnClickListener(v -> {
            navController.navigate(R.id.addRecipeFragment);
        });

        mBinding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
        });

//        mRecyclerView = mBinding.recyclerViewCategories;
//
//        setupRecyclerView(new ArrayList<>());
//
//        FirebaseFirestore.getInstance().collection("categories").addSnapshotListener(
//                (documentSnapshots, error) -> {
//
//                    List<DocumentSnapshot> docs = documentSnapshots.getDocuments();
//                    ArrayList<Category> categories = new ArrayList<>();
//                    for (int i = 0; i < docs.size(); i++) {
//                        DocumentSnapshot documentSnapshot = docs.get(i);
//        //                documentSnapshot.toObject(Category.class);
//                        categories.add(Category.fromDocument(documentSnapshot));
//                    }
//                    setupRecyclerView(categories);
//
//        });
    }

    private void setupRecyclerView(ArrayList<Category> categories){
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}