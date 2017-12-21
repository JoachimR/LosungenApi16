package de.reiss.android.losungen.preferences

import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers
import de.reiss.android.losungen.R
import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.util.extensions.isPlayServiceAvailable


class AppPreferencesFragment : PreferenceFragmentCompatDividers() {

    companion object {

        private const val LIST_LANGUAGES = "LIST_LANGUAGES"

        fun newInstance(languages: List<Language>) =
                AppPreferencesFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(LIST_LANGUAGES,
                                arrayListOf<Language>().apply { addAll(languages) })
                    }
                }

    }

    private lateinit var languages: List<Language>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languages = arguments?.getParcelableArrayList(LIST_LANGUAGES) ?: arrayListOf()
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            return super.onCreateView(inflater, container, savedInstanceState)
        } finally {
            setDividerPreferences(PreferenceFragmentCompatDividers.DIVIDER_PADDING_CHILD
                    or PreferenceFragmentCompatDividers.DIVIDER_CATEGORY_AFTER_LAST
                    or PreferenceFragmentCompatDividers.DIVIDER_CATEGORY_BETWEEN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (findPreference(getString(R.string.pref_language_key)) as ListPreference).apply {
            entries = languages.map { it.name }.toTypedArray()
            entryValues = languages.map { it.key }.toTypedArray()
        }

        findPreference(getString(R.string.pref_show_daily_notification_key)).apply {
            val playServiceAvailable = context.isPlayServiceAvailable()
            isVisible = playServiceAvailable
            setDefaultValue(playServiceAvailable)
        }
    }

}
