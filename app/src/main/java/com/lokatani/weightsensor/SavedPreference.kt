package com.lokatani.weightsensor

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object SavedPreference {
    private const val EMAIL_KEY = "email"
    private const val USERNAME_KEY = "username"

    private fun getSharedPreference(ctx: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun editor(context: Context, key: String, value: String) {
        getSharedPreference(context).edit().putString(key, value).apply()
    }

    fun getEmail(context: Context): String? {
        return getSharedPreference(context).getString(EMAIL_KEY, "")
    }

    fun setEmail(context: Context, email: String) {
        editor(context, EMAIL_KEY, email)
    }

    fun getUsername(context: Context): String? {
        return getSharedPreference(context).getString(USERNAME_KEY, "")
    }

    fun setUsername(context: Context, username: String) {
        editor(context, USERNAME_KEY, username)
    }
    
    fun clearPreference(context: Context) {
        getSharedPreference(context).edit().clear().apply()
    }
}