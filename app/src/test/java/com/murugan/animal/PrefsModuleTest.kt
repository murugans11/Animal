package com.murugan.animal

import android.app.Application
import com.murugan.animal.di.PrefModule
import com.murugan.animal.utils.SharedPreferencesHelper

class PrefsModuleTest(private val mockPrefs: SharedPreferencesHelper): PrefModule() {

    override fun providePref(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }

}