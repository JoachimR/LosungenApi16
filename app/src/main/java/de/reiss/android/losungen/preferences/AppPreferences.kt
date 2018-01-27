package de.reiss.android.losungen.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Typeface
import android.preference.PreferenceManager
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import de.reiss.android.losungen.R
import de.reiss.android.losungen.events.AppStyleChanged
import de.reiss.android.losungen.events.postMessageEvent
import de.reiss.android.losungen.util.extensions.change
import de.reiss.android.losungen.widget.triggerWidgetUpdate

open class AppPreferences(val context: Context) : OnSharedPreferenceChangeListener {

    val typefaces = mutableMapOf<String, Typeface>().apply {
        put(str(R.string.typeface_default), Typeface.DEFAULT)
        put(str(R.string.typeface_default_bold), Typeface.DEFAULT_BOLD)
        put(str(R.string.typeface_monospace), Typeface.MONOSPACE)
        put(str(R.string.typeface_sans_serif), Typeface.SANS_SERIF)
        put(str(R.string.typeface_serif), Typeface.SERIF)

        ResourcesCompat.getFont(context, R.font.calligraphic)?.let {
            put(str(R.string.typeface_calligraphic), it)
        }
        ResourcesCompat.getFont(context, R.font.cloister)?.let {
            put(str(R.string.typeface_cloister), it)
        }
        ResourcesCompat.getFont(context, R.font.comic)?.let {
            put(str(R.string.typeface_comic), it)
        }
        ResourcesCompat.getFont(context, R.font.gothic)?.let {
            put(str(R.string.typeface_gothic), it)
        }
        ResourcesCompat.getFont(context, R.font.grunge)?.let {
            put(str(R.string.typeface_grunge), it)
        }
        ResourcesCompat.getFont(context, R.font.handdrawn)?.let {
            put(str(R.string.typeface_handdrawn), it)
        }
        ResourcesCompat.getFont(context, R.font.typewriter)?.let {
            put(str(R.string.typeface_typewriter), it)
        }
        ResourcesCompat.getFont(context, R.font.alegreya_sc)?.let {
            put(str(R.string.typeface_alegreya_sc), it)
        }
        ResourcesCompat.getFont(context, R.font.baloo)?.let {
            put(str(R.string.typeface_baloo), it)
        }
        ResourcesCompat.getFont(context, R.font.dynalight)?.let {
            put(str(R.string.typeface_dynalight), it)
        }
        ResourcesCompat.getFont(context, R.font.finger_paint)?.let {
            put(str(R.string.typeface_finger_paint), it)
        }
        ResourcesCompat.getFont(context, R.font.just_me_again_down_here)?.let {
            put(str(R.string.typeface_just_me_again_down_here), it)
        }
        ResourcesCompat.getFont(context, R.font.nova_flat)?.let {
            put(str(R.string.typeface_nova_flat), it)
        }
        ResourcesCompat.getFont(context, R.font.sofia)?.let {
            put(str(R.string.typeface_sofia), it)
        }
        ResourcesCompat.getFont(context, R.font.vast_shadow)?.let {
            put(str(R.string.typeface_vast_shadow), it)
        }
        ResourcesCompat.getFont(context, R.font.londrina_shadow)?.let {
            put(str(R.string.typeface_londrina_shadow), it)
        }
    }

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        registerListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (sharedPreferences == preferences) {
            if (isWidgetPref(key)) {
                triggerWidgetUpdate()
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

    var typefaceString: String?
        get() = prefString(R.string.pref_font_typeface_key)
        set(chosenTypeface) = preferences.change {
            putString(str(R.string.pref_font_typeface_key),
                    if (typefaces.containsKey(chosenTypeface)) chosenTypeface
                    else str(R.string.typeface_default))
        }

    fun typeface(): Typeface = typefaces[typefaceString]
            ?: typefaces[str(R.string.typeface_default)]!!

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