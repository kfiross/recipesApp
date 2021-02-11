package com.recipesapp.recipesapp.views.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.recipesapp.recipesapp.R;
import com.recipesapp.recipesapp.databinding.FragmentLoginBinding;
import com.recipesapp.recipesapp.utils.LoginData;

public class LoginFragment extends Fragment {

    private NavController mNavController;
    private FirebaseAuth mAuth;
    private FragmentLoginBinding mBinding;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.setFragment(this);
        mBinding.setIsLoading(false);
        mBinding.setLoginMode(true);
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        mBinding.setLogin(new LoginData("1@1.com", "123456"));
    }


//    private boolean checkAllFields() {
//        if (mBinding.etUsername.length() == 0) {
//            mBinding.etUsername.setError("This field is required");
//            return false;
//        }
//
//        if (mBinding.etPassword.length() == 0) {
//            mBinding.etPassword.setError("This field is required");
//            return false;
//        }
//
//        if (mBinding.etPassword.length() < 6) {
//            mBinding.etPassword.setError("Password must be minimum 6 characters");
//            return false;
//        }
//
//        // after all validation return true.
//        return true;
//    }

    public void loginOrRegister() {
        final String email = mBinding.getLogin().getEmail();
        final String password = mBinding.getLogin().getPassword();

        // show progress
        mBinding.setIsLoading(true);

        if(mBinding.getLoginMode()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        mBinding.setIsLoading(false);
                        if (task.isSuccessful()) {
                            /// showToast();
                        } else {
                            Exception e = task.getException();
                        }
                    });

        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(registerTask -> {
                        mBinding.setIsLoading(false);
                        if (registerTask.isSuccessful()) {

                        } else {
                            // showAlertDialog();
                        }
                    });
        }
    }

    public void changeMode(){
        mBinding.setLoginMode(!mBinding.getLoginMode());
    }
}


