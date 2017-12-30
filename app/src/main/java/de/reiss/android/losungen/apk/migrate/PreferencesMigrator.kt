package de.reiss.android.losungen.apk.migrate

import android.content.Context
import de.reiss.android.losungen.R
import de.reiss.android.losungen.allLanguages
import de.reiss.android.losungen.preferences.AppPreferences
import javax.inject.Inject

class PreferencesMigrator @Inject constructor(val context: Context,
                                              val appPreferences: AppPreferences) {

    fun migrateAppPreferences() {
        val preferences = appPreferences.preferences
        val edit = preferences.edit()

        preferences.getString("pref_language_key", null)?.let { languageKey ->
            allLanguages.firstOrNull { it.key == languageKey }.let { reusable ->
                edit.putString(context.getString(R.string.pref_language_key), reusable?.key ?: "")
            }
        }

        preferences.getString("pref_typeface_key", null)?.let { typefaceKey ->

            val value = when (typefaceKey) {
                "normal" -> context.getString(R.string.typeface_default)
                "sansserif" -> context.getString(R.string.typeface_sans_serif)
                else -> typefaceKey
            }

            appPreferences.typefaces.map { it.key }.firstOrNull { it == value }?.let {
                appPreferences.typefaceString = it
            }
        }

        preferences.getBoolean("pref_actionbar_show_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_show_toolbar_key), it)
        }

        preferences.getBoolean("pref_personalnotes_show_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_show_notes_key), it)
        }

        // if prior version was used, then hide new card style at first
        preferences.getBoolean("alreadyLaunchedOncePrefKeyVersion2dot6", false).let {
            edit.putBoolean(context.getString(R.string.pref_show_cards_key), it.not())
        }

        edit.apply()
    }

    fun migrateWidgetPreferences() {
        val preferences = appPreferences.preferences
        val edit = preferences.edit()

        preferences.getBoolean("pref_widget_4x4_centeredtext_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_widget_centered_text_key), it)
        }

        preferences.getBoolean("pref_widget_4x4_showdate_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_widget_show_date_key), it)
        }

        edit.apply()
    }

    fun cleanOldPreferences(keysToKeep: List<String>) {
        val preferences = appPreferences.preferences
        val edit = preferences.edit()
        preferences.all
                .map { it.key }
                .filterNot { keysToKeep.contains(it) }
                .forEach {
                    edit.remove(it)
                }
        edit.apply()
    }

}