package de.reiss.android.losungen.main.viewpager

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.main.content.LosungFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount() = DaysPositionUtil.DAYS_OF_TIME

    override fun getItem(position: Int) = LosungFragment.createInstance(position)

}
