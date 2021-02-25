package com.foodiz.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.foodiz.app.databinding.ActivityMainBinding;
import com.foodiz.app.utils.FirestoreUtils;
import com.foodiz.app.utils.SharedPreferencesConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferencesConfig preferencesConfig;
    public static FragmentManager appFragmentManager;

    private NavController mNavController;
    private ActivityMainBinding binding;


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        appFragmentManager = getSupportFragmentManager();
        preferencesConfig = new SharedPreferencesConfig(this);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // listen to changes when user is logged in or sign out
        FirebaseAuth.getInstance().addAuthStateListener(this::handleAuth);



        preferencesConfig.setLocal("he",this);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavController.popBackStack();
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }



    /**
     * Function to handle auth events:
     * When user logged in, handles login process: reading data and saving to cache an
     * moving user to home screen.
     * When user logged out, handles logout procees: cleans cache and moving user to login screen
     */
    private void handleAuth(FirebaseAuth firebaseAuth) {
        NavOptions navOptions = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .build();

        // if user isn't logged in yet, or just logged out - mve him into Login screen
        // otherwise, he already logged in, so we move him immediately into Home screen
        int screenId = firebaseAuth.getCurrentUser() == null ? R.id.loginFragment : R.id.homeFragment;

        // update cached data
        if (firebaseAuth.getCurrentUser() != null) {
            initializeLocalData();
        }
        // clean cached data
        else {
            preferencesConfig.cleanAll();
        }

        mNavController.navigate(screenId, null, navOptions);
    }

    private void initializeLocalData() {
        FirestoreUtils.fetchUser().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult() == null){
                    preferencesConfig.writeFavsIds(null);
                    preferencesConfig.writeMyRecipesIds(null);
                }
                else {
                    DocumentSnapshot snapshot = task.getResult();
                    preferencesConfig.writeFavsIds((List<String>)snapshot.get("favourites"));
                    preferencesConfig.writeMyRecipesIds((List<String>)snapshot.get("my"));
                }
            }
        });
    }
}
