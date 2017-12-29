package de.reiss.android.losungen.main.daily.viewpager

import android.support.v4.app.FragmentManager

open class ViewPagerAdapterCreator {

    open fun create(fragmentManager: FragmentManager): ViewPagerAdapter =
            ViewPagerAdapter(fragmentManager)

}