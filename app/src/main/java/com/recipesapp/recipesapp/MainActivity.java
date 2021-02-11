package com.recipesapp.recipesapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.recipesapp.recipesapp.utils.FirestoreUtils;
import com.recipesapp.recipesapp.utils.SharedPreferencesConfig;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static SharedPreferencesConfig preferencesConfig;
    public static FragmentManager appFragmentManager;

    private NavController mNavController;
    private DrawerLayout drawer;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);

        appFragmentManager = getSupportFragmentManager();
        preferencesConfig = new SharedPreferencesConfig(this);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // listen to changes when user is logged in or sign out
        FirebaseAuth.getInstance().addAuthStateListener(this::handleAuth);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

//        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            switch (destination.getId()){
//                case R.id.homeFragment:
//                case R.id.favoritesFragment2:
//                case R.id.addRecipeFragment:
//
//                    break;
//
//                default:
//                    break;
//            }
//
//        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                switch (mNavController.getCurrentDestination().getId()) {
                    case R.id.homeFragment:
                    case R.id.favoritesFragment2:
                    case R.id.addRecipeFragment:
                    case R.id.myRecipesFragment:
                        drawer.open();
                        break;

                    default:
                        mNavController.popBackStack();
                        break;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

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
        FirestoreUtils.fetchMyFavs().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                preferencesConfig.writeFavsIds((List<String>) task.getResult().get("favourites"));
            }
        });
    }
}
