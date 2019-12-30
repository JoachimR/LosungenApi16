package de.reiss.android.losungen.architecture

import android.app.Activity
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


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
