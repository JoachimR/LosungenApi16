package de.reiss.android.losungen.architecture

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.DialogFragment
import android.view.View


abstract class AppDialogFragment<T : ViewModel> : DialogFragment() {

    var viewModelProvider: ViewModelProvider? = null

    lateinit var viewModel: T

    abstract fun defineViewModelProvider(): ViewModelProvider
    abstract fun defineViewModel(): T

    abstract fun initViews(layout: View)
    abstract fun initViewModelObservers()

    abstract fun title(): String

    abstract fun inflateLayout(activity: Activity): View

    fun loadViewModelProvider(): ViewModelProvider {
        if (viewModelProvider == null) {
            viewModelProvider = defineViewModelProvider()
        }
        return viewModelProvider!!
    }

    protected fun initLayout(activity: Activity): View {
        val layout = inflateLayout(activity)
        initViews(layout)
        initViewModel()
        return layout
    }

    private fun initViewModel() {
        viewModel = defineViewModel()
        initViewModelObservers()
    }

}
