package de.reiss.android.losungen

import android.app.Application
import androidx.preference.PreferenceManager
import de.reiss.android.losungen.architecture.di.ApplicationComponent
import de.reiss.android.losungen.architecture.di.ContextModule
import de.reiss.android.losungen.architecture.di.DaggerApplicationComponent
import de.reiss.android.losungen.architecture.di.DatabaseModule
import de.reiss.android.losungen.notification.NotificationService

open class App : Application() {

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
        NotificationService.schedule(this)
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

