package com.foodiz.app.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.foodiz.app.R;
import com.foodiz.app.databinding.FragmentLoginBinding;
import com.foodiz.app.utils.FirestoreUtils;
import com.foodiz.app.utils.LoginData;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnLoginRegister.btn.setOnClickListener(v -> loginOrRegister());
        mBinding.btnChangeLoginMode.setOnClickListener(v -> changeMode());

        mBinding.setFragment(this);
        mBinding.setIsLoading(false);
        mBinding.setLoginMode(true);
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        mBinding.setLogin(new LoginData("", "", ""));
    }

    public void loginOrRegister() {
        final String email = mBinding.getLogin().getEmail();
        final String password = mBinding.getLogin().getPassword();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getContext(), "Please Enter details", Toast.LENGTH_SHORT).show();
            return;
        }
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
                        updateName();
                        mBinding.setIsLoading(false);
                        if (registerTask.isSuccessful()) {
                            FirestoreUtils.initUserData(registerTask.getResult().getUser().getUid());
                        } else {
                            // showAlertDialog();
                        }
                    });
        }
    }

    public void changeMode(){
        mBinding.setLoginMode(!mBinding.getLoginMode());
    }

    private void updateName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String name = mBinding.getLogin().getName();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                    }
                });

    }
}


