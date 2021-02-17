package com.recipesapp.recipesapp.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.data.model.Category;
import com.recipesapp.recipesapp.databinding.FragmentHomeBinding;
import com.recipesapp.recipesapp.views.adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

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

        mRecyclerView = mBinding.recyclerViewCategories;

        setupRecyclerView(new ArrayList<>());

        FirebaseFirestore.getInstance().collection("categories").addSnapshotListener(
                (documentSnapshots, error) -> {

                    List<DocumentSnapshot> docs = documentSnapshots.getDocuments();
                    ArrayList<Category> categories = new ArrayList<>();
                    for (int i = 0; i < docs.size(); i++) {
                        DocumentSnapshot documentSnapshot = docs.get(i);
        //                documentSnapshot.toObject(Category.class);
                        categories.add(Category.fromDocument(documentSnapshot));
                    }
                    setupRecyclerView(categories);

        });
    }

    private void setupRecyclerView(ArrayList<Category> categories){
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(categoryAdapter);
    }
}