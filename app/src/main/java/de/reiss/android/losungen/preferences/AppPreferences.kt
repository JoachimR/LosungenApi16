package de.reiss.android.losungen.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.preference.PreferenceManager
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.events.AppStyleChanged
import de.reiss.android.losungen.events.postMessageEvent
import de.reiss.android.losungen.util.extensions.change
import de.reiss.android.losungen.widget.WidgetRefresher

open class AppPreferences(val context: Context) : OnSharedPreferenceChangeListener {

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val widgetRefresher: WidgetRefresher by lazy {
        App.component.widgetRefresher
    }

    init {
        registerListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (sharedPreferences == preferences) {
            if (isWidgetPref(key)) {
                widgetRefresher.execute()
            } else {
                if (key == str(R.string.pref_font_size_key)
                        || key == str(R.string.pref_font_color_key)
                        || key == str(R.string.pref_background_color_key)
                        || key == str(R.string.pref_card_background_color_key)) {
                    postMessageEvent(AppStyleChanged())
                }
            }
        }
    }

    private fun isWidgetPref(key: String): Boolean = (key == str(R.string.pref_language_key)
            || key == str(R.string.pref_widget_font_size_key)
            || key == str(R.string.pref_widget_font_color_key)
            || key == str(R.string.pref_widget_background_color_key)
            || key == str(R.string.pref_widget_show_date_key)
            || key == str(R.string.pref_widget_centered_text_key))

    fun unregisterListener(listener: OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun registerListener(listener: OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    open var chosenLanguage: String?
        get() = prefString(R.string.pref_language_key)
        set(chosenLanguage) = preferences.change {
            putString(str(R.string.pref_language_key), chosenLanguage)
        }

    fun currentTheme(): AppTheme {
        val chosenTheme = prefString(R.string.pref_theme_key, R.string.pref_theme_default)
        return AppTheme.find(context, chosenTheme) ?: AppTheme.ORANGE_BLUE
    }

    fun showNotes() = prefBoolean(R.string.pref_show_notes_key, true)

    fun showToolbar() = prefBoolean(R.string.pref_show_toolbar_key, true)

    fun showCards() = prefBoolean(R.string.pref_show_cards_key, true)

    fun shouldShowDailyNotification() =
            prefBoolean(R.string.pref_show_daily_notification_key, false)

    fun fontSize() = prefInt(
            stringRes = R.string.pref_font_size_key,
            default = Integer.parseInt(str(R.string.pref_font_size_default)))

    fun fontColor() = prefInt(R.string.pref_font_color_key,
            ContextCompat.getColor(context, R.color.default_font_color))

    fun backgroundColor() = prefInt(R.string.pref_background_color_key,
            ContextCompat.getColor(context, R.color.default_background_color))

    fun cardBackgroundColor() = prefInt(R.string.pref_card_background_color_key,
            ContextCompat.getColor(context, R.color.default_card_background_color))

    fun widgetShowDate() = prefBoolean(R.string.pref_widget_show_date_key, true)

    fun widgetFontColor() = prefInt(R.string.pref_widget_font_color_key,
            ContextCompat.getColor(context, R.color.default_widget_font_color))

    fun widgetFontSize() = prefInt(
            stringRes = R.string.pref_widget_font_size_key,
            default = Integer.parseInt(str(R.string.pref_widget_font_size_default))).toFloat()

    fun widgetCentered() = prefBoolean(R.string.pref_widget_centered_text_key, true)

    fun widgetBackground(): String = prefString(R.string.pref_widget_background_color_key,
            R.string.pref_widget_background_color_default)

    fun changeFontSize(newFontSize: Int) {
        val min = Integer.parseInt(str(R.string.pref_font_size_min))
        val max = Integer.parseInt(str(R.string.pref_font_size_max))
        val changeValue = if (newFontSize < min) min else if (newFontSize > max) max else newFontSize
        preferences.change {
            putInt(str(R.string.pref_font_size_key), changeValue)
        }
    }

    private fun prefString(@StringRes stringRes: Int, @StringRes defaultStringRes: Int? = null) =
            preferences.getString(str(stringRes),
                    if (defaultStringRes != null) str(defaultStringRes) else null)

    private fun prefBoolean(@StringRes stringRes: Int, default: Boolean) =
            preferences.getBoolean(str(stringRes), default)

    private fun prefInt(@StringRes stringRes: Int, default: Int) =
            preferences.getInt(str(stringRes), default)

    private fun str(@StringRes stringRes: Int) = context.getString(stringRes)

}