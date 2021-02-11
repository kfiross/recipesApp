package com.recipesapp.recipesapp.utils

import android.content.Context
import android.content.SharedPreferences

import android.content.Context.MODE_PRIVATE
import com.recipesapp.recipesapp.R

/**
 * Helper class to handle read/write from/to Shared Preferences
 */
class SharedPreferencesConfig(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.resources.getString(R.string.login_preferences),
        MODE_PRIVATE
    )


    fun readLangStatus() = sharedPreferences.getString("curr_lang", "en")!!

    fun writeLangStatus(status: String) {
        val editor = sharedPreferences.edit()
        editor.putString("curr_lang", status)
        editor.apply()
    }
}