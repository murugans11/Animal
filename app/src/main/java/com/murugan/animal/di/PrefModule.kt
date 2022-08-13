package com.murugan.animal.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.murugan.animal.utils.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class PrefModule {

    @Provides
    @Singleton
    @TypeOfQualifier(CONTEXT_APP)
    fun providePref(app: Application): SharedPreferencesHelper {
        return SharedPreferencesHelper(app)
    }

    @Provides
    @TypeOfQualifier(CONTEXT_ACTIVITY)
    fun provideActivitySharedPreferenceHelper(activity: AppCompatActivity): SharedPreferencesHelper {
        return SharedPreferencesHelper(activity)
    }

}

const val CONTEXT_APP = "Application Context"
const val CONTEXT_ACTIVITY = "Activity Context"

@Qualifier
annotation class TypeOfQualifier(val type: String)