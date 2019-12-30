package de.reiss.android.losungen.main.daily.viewpager

import androidx.fragment.app.FragmentManager

open class ViewPagerAdapterCreator {

    open fun create(fragmentManager: FragmentManager): ViewPagerAdapter =
            ViewPagerAdapter(fragmentManager)

}