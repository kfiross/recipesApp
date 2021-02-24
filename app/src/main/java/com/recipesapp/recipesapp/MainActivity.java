package com.recipesapp.recipesapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.recipesapp.recipesapp.databinding.ActivityMainBinding;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.utils.SharedPreferencesConfig;

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


        BottomNavigationView navigationView = binding.bttmNav;
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                // navigate to home
                case R.id.nav_home:
                    mNavController.navigate(R.id.homeFragment);
                    break;

                // navigate to favourites
                case R.id.nav_favourites:
                    mNavController.navigate(R.id.favoritesFragment2);
                    break;

                // navigate to add a new recipe
                case R.id.nav_add_recipe:
                    mNavController.navigate(R.id.addRecipeFragment);
                    break;

                case R.id.nav_my_recipes:
                    mNavController.navigate(R.id.myRecipesFragment);
                    break;

                case R.id.nav_search_recipe:
                    mNavController.navigate(R.id.searchFragment);
                    break;
            }
            return true;
        });

        // listen to changes in navigation
        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId() == R.id.loginFragment){
                binding.bttmNav.setVisibility(View.GONE);
            }
            else {
                binding.bttmNav.setVisibility(View.VISIBLE);
            }
        });

        preferencesConfig.setLocal("he",this);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.navigation_menu);
        navigationView.setSelectedItemId(R.id.nav_home);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavController.popBackStack();
                break;

            case R.id.action_menu_settings:
                break;

            case R.id.action_menu_logout:
                FirebaseAuth.getInstance().signOut();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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

//            navHeaderMainBinding.setName(firebaseAuth.getCurrentUser().getDisplayName());
//            navHeaderMainBinding.setEmail(firebaseAuth.getCurrentUser().getEmail());
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
