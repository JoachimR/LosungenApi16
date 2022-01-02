package de.reiss.android.losungen.architecture.di

import android.app.Application
import dagger.Module
import dagger.Provides
import de.reiss.android.losungen.database.*

@Module
open class DatabaseModule(private val application: Application) {

    open fun getDatabase(): LosungDatabase = LosungDatabase.create(application)

    @Provides
    @ApplicationScope
    open fun losungDatabase(): LosungDatabase =
        getDatabase()

    @Provides
    @ApplicationScope
    open fun dailyLosungItemDao(): DailyLosungItemDao =
        getDatabase().dailyLosungItemDao()

    @Provides
    @ApplicationScope
    open fun weeklyLosungItemDao(): WeeklyLosungItemDao =
        getDatabase().weeklyLosungItemDao()

    @Provides
    @ApplicationScope
    open fun monthlyLosungItemDao(): MonthlyLosungItemDao =
        getDatabase().monthlyLosungItemDao()

    @Provides
    @ApplicationScope
    open fun yearlyLosungItemDao(): YearlyLosungItemDao =
        getDatabase().yearlyLosungItemDao()

    @Provides
    @ApplicationScope
    open fun languageItemDao(): LanguageItemDao =
        getDatabase().languageItemDao()

    @Provides
    @ApplicationScope
    open fun noteItemDao(): NoteItemDao =
        getDatabase().noteItemDao()

}
