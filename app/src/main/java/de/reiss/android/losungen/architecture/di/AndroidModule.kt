package de.reiss.android.losungen.architecture.di

import android.app.NotificationManager
import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import dagger.Module
import dagger.Provides


@Module(includes = arrayOf(ContextModule::class))
class AndroidModule {

    @Provides
    @ApplicationScope
    fun notificationManager(context: Context) =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

    @Provides
    @ApplicationScope
    fun clipboardManager(context: Context) =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    @ApplicationScope
    fun searchManager(context: Context) =
            context.getSystemService(Context.SEARCH_SERVICE) as SearchManager

}