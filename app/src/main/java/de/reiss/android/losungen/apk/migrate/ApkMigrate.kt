package de.reiss.android.losungen.apk.migrate

import android.content.Context
import de.reiss.android.losungen.R
import de.reiss.android.losungen.apk.prepare.ApkPrepare
import de.reiss.android.losungen.logger.logErrorWithCrashlytics
import de.reiss.android.losungen.preferences.AppPreferences
import javax.inject.Inject

class ApkMigrate @Inject constructor(val context: Context,
                                     val appPreferences: AppPreferences,
                                     val preferencesMigrator: PreferencesMigrator,
                                     val databaseMigrator: DatabaseMigrator) {

    companion object {

        const val migrateKey = "PERFORMED_MIGRATION_TO_1001";

    }

    private val keysAfterMigration: List<String> by lazy {
        listOf<String>(
                ApkPrepare.initKey,
                migrateKey,
                context.getString(R.string.pref_theme_key),
                context.getString(R.string.pref_language_key),
                context.getString(R.string.pref_show_toolbar_key),
                context.getString(R.string.pref_show_cards_key),
                context.getString(R.string.pref_show_notes_key),
                context.getString(R.string.pref_font_size_key),
                context.getString(R.string.pref_font_color_key),
                context.getString(R.string.pref_background_color_key),
                context.getString(R.string.pref_card_background_color_key),
                context.getString(R.string.pref_widget_background_color_key),
                context.getString(R.string.pref_widget_font_color_key),
                context.getString(R.string.pref_widget_font_size_key),
                context.getString(R.string.pref_widget_show_date_key),
                context.getString(R.string.pref_widget_centered_text_key),
                context.getString(R.string.pref_show_daily_notification_key)
        )
    }

    fun migrateIfNeeded() {
        val preferences = appPreferences.preferences

        try {
            val alreadyMigrated = preferences.getBoolean(migrateKey, false)
            if (alreadyMigrated.not()) {
                preferences.edit().putBoolean(migrateKey, true).apply()

                with(preferencesMigrator) {
                    migrateAppPreferences()
                    migrateWidgetPreferences()
                    cleanOldPreferences(keysToKeep = keysAfterMigration)
                }

                with(databaseMigrator) {
                    migrateNotesDatabase()
                }
            }
        } catch (e: Exception) {
            logErrorWithCrashlytics(e) {
                "error when trying to migrate preferences to version app 126"
            }
        }
    }

}