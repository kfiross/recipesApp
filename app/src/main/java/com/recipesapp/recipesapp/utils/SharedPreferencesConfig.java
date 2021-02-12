package com.recipesapp.recipesapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.recipesapp.recipesapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
        if(ids == null){
            editor.putStringSet("favs_ids", new HashSet<>());
        }
        else{
            editor.putStringSet("favs_ids", new HashSet<>(ids));
        }
        editor.apply();
    }

    /// ///////////////////////////

    public Set<String> readMyRecipesIds(){
        return sharedPreferences.getStringSet("my_recipes_ids", new HashSet<>());
    }

    public void addIdToMyRecipes(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readFavsIds();
        ids.add(id);
        editor.putStringSet("my_recipes_ids", ids);
        editor.apply();
    }

    public void removeIdFromMyRecipes(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readFavsIds();
        ids.remove(id);
        editor.putStringSet("my_recipes_ids", ids);
        editor.apply();
    }

    public void writeMyRecipesIds(List<String> ids){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(ids == null){
            editor.putStringSet("my_recipes_ids", new HashSet<>());
        }
        else{
            editor.putStringSet("my_recipes_ids", new HashSet<>(ids));
        }
        editor.apply();
    }

    ///////

    public String readLangStatus() {
        return sharedPreferences.getString("curr_lang", "he");
    }

    public void writeLangStatus(String status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("curr_lang", status);
        editor.apply();
    }

    public void setLocal(String lang, Context context) {
        String localString = localFromLang(lang);
        Locale local = new Locale(localString);
        Locale.setDefault(local);
        Configuration configuration = new Configuration();
        configuration.setLocale(local);
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        writeLangStatus(lang);
    }

    public void loadLocal(Context context){
        String lang = readLangStatus();
        setLocal("he", context);
    }

    public String localFromLang(String lang) {
        if (lang.equals("he"))
            return "iw";
        else
            return lang;
    }
}