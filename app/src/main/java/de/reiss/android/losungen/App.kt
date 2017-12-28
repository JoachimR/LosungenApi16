package de.reiss.android.losungen

import android.app.Application
import android.support.v7.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import de.reiss.android.losungen.architecture.di.ApplicationComponent
import de.reiss.android.losungen.architecture.di.ContextModule
import de.reiss.android.losungen.architecture.di.DaggerApplicationComponent
import de.reiss.android.losungen.architecture.di.DatabaseModule
import de.reiss.android.losungen.notification.NotificationService
import io.fabric.sdk.android.Fabric

open class App : Application() {

    companion object {

        @JvmStatic lateinit var component: ApplicationComponent

    }

    override fun onCreate() {
        super.onCreate()
        component = createComponent()
        initApp()
    }

    open fun initApp() {
        Fabric.with(this, Crashlytics.Builder()
                .core(CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG)
                        .build())
                .build())
        NotificationService.schedule(this)
        initPrefs()
        component.widgetRefresher.execute()
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

