package de.reiss.android.losungen.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.android.losungen.util.extensions.displayDialog


abstract class AppFragment<T : ViewModel>(@LayoutRes private val fragmentLayout: Int) : Fragment() {

    var viewModelProvider: ViewModelProvider? = null

    lateinit var viewModel: T

    abstract fun defineViewModelProvider(): ViewModelProvider
    abstract fun defineViewModel(): T

    abstract fun initViews(layout: View)
    abstract fun initViewModelObservers()

    open fun onAppFragmentReady() {
    }

    fun loadViewModelProvider(): ViewModelProvider {
        if (viewModelProvider == null) {
            viewModelProvider = defineViewModelProvider()
        }
        return viewModelProvider!!
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(fragmentLayout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = defineViewModel()
        initViewModelObservers()
        onAppFragmentReady()
    }

    protected fun displayDialog(dialogFragment: DialogFragment) {
        (activity as? AppCompatActivity?)?.displayDialog(dialogFragment)
    }

}
