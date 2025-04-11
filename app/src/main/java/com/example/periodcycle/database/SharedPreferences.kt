package com.example.periodcycle.database

import android.content.Context
import android.util.Log

object SharedPreferences {
        private const val PREF_NAME = "app_preferences"
        private const val USER_SAVED_KEY = "user_saved_key"

        fun isUserSaved(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(USER_SAVED_KEY, false)
        }

        fun setUserSaved(context: Context, isSaved: Boolean) {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(USER_SAVED_KEY, isSaved).apply()
        }
}

object SharedPreferences2 {
    private const val PREF_NAME = "app_preferences2"
    private const val USER_SAVED_KEY = "user_saved_key2"

    fun isUserSaved(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        Log.d("SAVE_DATA", Context.MODE_PRIVATE.toString())
        return sharedPreferences.getBoolean(USER_SAVED_KEY, false)
    }

    fun setUserSaved(context: Context, isSaved: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(USER_SAVED_KEY, isSaved).apply()
    }
}