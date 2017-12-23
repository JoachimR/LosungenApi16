package de.reiss.android.losungen.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import de.reiss.android.losungen.R
import de.reiss.android.losungen.SplashScreenActivity
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class NotificationHelper @Inject constructor(private val context: Context,
                                                  private val notificationManager: NotificationManager,
                                                  private val executor: Executor,
                                                  private val appPreferences: AppPreferences,
                                                  private val dailyLosungItemDao: DailyLosungItemDao,
                                                  private val languageItemDao: LanguageItemDao) {

    companion object {

        val NOTIFICATION_CHANNEL_ID = "Losung"
        val NOTIFICATION_CHANNEL_NAME = "Daily Losung"

        val NOTIFICATION_ID = 7

    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
    }

    fun tryShowNotification() {
        if (appPreferences.shouldShowDailyNotification().not()) {
            return
        }
        val chosenLanguage = appPreferences.chosenLanguage ?: return

        executor.execute {

            languageItemDao.find(chosenLanguage)?.let { languageItem ->

                val item = dailyLosungItemDao.byDate(languageItem.id, Date().withZeroDayTime())
                Converter.itemToDailyLosung(languageItem.language, item)?.let {
                    showNotification(it)
                }
            }
        }
    }

    private fun showNotification(dailyLosung: DailyLosung) {
        notificationManager.notify(NOTIFICATION_ID, createNotification(context, dailyLosung))
    }

    private fun createNotification(context: Context, dailyLosung: DailyLosung) =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_daily_losung)
                    .setContentTitle(formattedDate(context, dailyLosung.startDate().time))
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(losungToText(dailyLosung)))
                    .setLargeIcon(BitmapFactory.decodeResource(
                            context.resources, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent(context))
                    .build()

    private fun pendingIntent(context: Context) =
            PendingIntent.getActivity(context, createUniqueRequestCode(),
                    SplashScreenActivity.createIntent(context)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT)

    private fun createUniqueRequestCode() = Random().nextInt(100)

    private fun losungToText(dailyLosung: DailyLosung) =
            StringBuilder().apply {
                append(dailyLosung.bibleTextPair.first.text)
                append(" ")
                append(dailyLosung.bibleTextPair.first.source)
                append("\n")
                append(dailyLosung.bibleTextPair.second.text)
                append(" ")
                append(dailyLosung.bibleTextPair.second.source)
            }.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW).apply {
            enableLights(false)
            enableVibration(false)
            setShowBadge(false)
        }.let {
            notificationManager.createNotificationChannel(it)
        }
    }

}