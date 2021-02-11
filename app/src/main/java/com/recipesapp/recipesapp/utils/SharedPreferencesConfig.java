package com.recipesapp.recipesapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.recipesapp.recipesapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Helper class to handle read/write from/to Shared Preferences
 */
public class SharedPreferencesConfig {
    private SharedPreferences sharedPreferences;

    public SharedPreferencesConfig(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                context.getResources().getString(R.string.login_preferences), MODE_PRIVATE);

    }

    public void cleanAll(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public Set<String> readFavsIds(){
        return sharedPreferences.getStringSet("favs_ids", new HashSet<>());
    }

    public void addFavId(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readFavsIds();
        ids.add(id);
        editor.putStringSet("favs_ids", ids);
        editor.apply();
    }

    public void removeFavId(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readFavsIds();
        ids.remove(id);
        editor.putStringSet("favs_ids", ids);
        editor.apply();
    }

    public void writeFavsIds(List<String> ids){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("favs_ids", new HashSet<>(ids));
        editor.apply();
    }
}