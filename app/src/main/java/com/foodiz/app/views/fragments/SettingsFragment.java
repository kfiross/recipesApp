package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.foodiz.app.R;
import com.foodiz.app.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding mBinding;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);

        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsOnlyFragment())
                .commit();

        mBinding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
        });
    }
}
