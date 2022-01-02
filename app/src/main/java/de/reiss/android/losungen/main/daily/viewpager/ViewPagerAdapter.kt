package de.reiss.android.losungen.main.daily.viewpager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.main.daily.LosungFragmentNoCards
import de.reiss.android.losungen.main.daily.LosungFragmentWithCards
import de.reiss.android.losungen.preferences.AppPreferences

open class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    override fun getCount() = DaysPositionUtil.DAYS_OF_TIME

    override fun getItem(position: Int) =
        if (appPreferences.showCards()) {
            if (appPreferences.showCards()) {
                LosungFragmentWithCards.createInstance(position)
            } else {
                LosungFragmentWithCards.createInstance(position)
            }
        } else {
            LosungFragmentNoCards.createInstance(position)
        }

}
