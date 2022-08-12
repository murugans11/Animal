package com.murugan.animal.utils

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {

    private val PREF_API_KEI = "Api Key"

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun saveApiKey(key: String?) = prefs.edit().putString(PREF_API_KEI, key).apply()

    fun getApiKey() = prefs.getString(PREF_API_KEI, null)

}