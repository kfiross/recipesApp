package com.foodiz.app.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.foodiz.app.R;

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

    /**
     * clears all saved preferences
     */
    public void cleanAll(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * return the list of ids of the user's favourites recipes
     */
    public Set<String> readFavsIds(){
        return sharedPreferences.getStringSet("favs_ids", new HashSet<>());
    }

    /**
     * adds a new favourite recipe id
     */
    public void addFavId(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readFavsIds();
        ids.add(id);
        editor.putStringSet("favs_ids", ids);
        editor.apply();
    }

    /**
     * removes a favourite (assume exist) recipe id
     */
    public void removeFavId(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readFavsIds();
        ids.remove(id);
        editor.putStringSet("favs_ids", ids);
        editor.apply();
    }

    /**
     * updates the user's favourites recipes ids list
     */
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

    /**
     * return the list of ids of the recipes user created
     */
    public Set<String> readMyRecipesIds(){
        return sharedPreferences.getStringSet("my_recipes_ids", new HashSet<>());
    }

    /**
     * adds a new user-created recipe id
     */
    public void addIdToMyRecipes(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readMyRecipesIds();
        ids.add(id);
        editor.putStringSet("my_recipes_ids", ids);
        editor.apply();
    }

    /**
     * remove a new user-created recipe id
     */
    public void removeIdFromMyRecipes(String id){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = readMyRecipesIds();
        ids.remove(id);
        editor.putStringSet("my_recipes_ids", ids);
        editor.apply();
    }


    /**
     * updates the user-created recipes ids list
     */
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

    /**
     * return the current used language (code)
     */
    public String readLangStatus() {
        return sharedPreferences.getString("curr_lang", "he");
    }

    /**
     * updates the current used language (code)
     */
    public void writeLangStatus(String status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("curr_lang", status);
        editor.apply();
    }

    /**
     * set the locale of the app
     */
    public void setLocal(String lang, Context context) {
        String localString = localFromLang(lang);
        Locale local = new Locale(localString);
        Locale.setDefault(local);
        Configuration configuration = new Configuration();
        configuration.setLocale(local);
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        writeLangStatus(lang);
    }

    /**
     * update the locale of the app
     */
    public void loadLocal(Context context){
        String lang = readLangStatus();
        setLocal(lang, context);
    }

    /**
     * converting the locale of from a language Code
     */
    public String localFromLang(String lang) {
        if (lang.equals("he"))
            return "iw";
        else
            return lang;
    }
}