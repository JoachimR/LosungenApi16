package de.reiss.android.losungen.architecture.di

import android.app.Application
import dagger.Module
import dagger.Provides
import de.reiss.android.losungen.database.LosungDatabase

@Module
open class DatabaseModule(val application: Application) {

    open fun getDatabase(): LosungDatabase = LosungDatabase.create(application)

    @Provides
    @ApplicationScope
    open fun losungDatabase() = getDatabase()

    @Provides
    @ApplicationScope
    open fun dailyLosungItemDao() = getDatabase().dailyLosungItemDao()

    @Provides
    @ApplicationScope
    open fun languageItemDao() = getDatabase().languageItemDao()

    @Provides
    @ApplicationScope
    open fun noteItemDao() = getDatabase().noteItemDao()

}
