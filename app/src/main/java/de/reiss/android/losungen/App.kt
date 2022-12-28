package de.reiss.android.losungen

import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import de.reiss.android.losungen.architecture.di.ApplicationComponent
import de.reiss.android.losungen.architecture.di.ContextModule
import de.reiss.android.losungen.architecture.di.DaggerApplicationComponent
import de.reiss.android.losungen.architecture.di.DatabaseModule

open class App : MultiDexApplication() {

    companion object {

        @JvmStatic
        lateinit var component: ApplicationComponent

    }

    override fun onCreate() {
        super.onCreate()
        component = createComponent()
        initApp()
    }

    open fun initApp() {
        initPrefs()
    }

    private fun initPrefs() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        component.apkHandler.handleApk()
    }

    open fun createComponent(): ApplicationComponent =
        DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .databaseModule(DatabaseModule(this))
            .build()

}

