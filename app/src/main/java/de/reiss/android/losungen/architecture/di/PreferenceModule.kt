package de.reiss.android.losungen.architecture.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.reiss.android.losungen.preferences.AppPreferences

@Module(includes = arrayOf(ContextModule::class))
class PreferenceModule {

    @Provides
    @ApplicationScope
    fun appPreferences(context: Context): AppPreferences = AppPreferences(context)

}