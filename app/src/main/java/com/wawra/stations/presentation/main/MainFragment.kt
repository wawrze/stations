package com.wawra.stations.presentation.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.wawra.stations.R
import com.wawra.stations.base.BaseFragment
import com.wawra.stations.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import javax.inject.Inject

class MainFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var station1Adapter: AutoCompleteAdapter
    private lateinit var station2Adapter: AutoCompleteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        setupAdapters()
        setupObservers()
        view?.clearFocus()
    }

    private fun setupAdapters() {
        context?.let {
            station1Adapter = AutoCompleteAdapter(it, ArrayList())
            station2Adapter = AutoCompleteAdapter(it, ArrayList())
        }
        setupAutoCompleteTextView(fragment_main_station_input_1, true)
        setupAutoCompleteTextView(fragment_main_station_input_2, false)
    }

    private fun setupAutoCompleteTextView(view: AutoCompleteTextView, forStation1: Boolean) {
        view.apply {
            setAdapter(if (forStation1) station1Adapter else station2Adapter)
            threshold = 2
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showDropDown() else dismissDropDown()
            }
            addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // TODO: show progress bar
                    viewModel.getMatchingStations(s.toString(), forStation1)
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.stations1.observe {
            station1Adapter.clear()
            station1Adapter.addAll(it)
        }
        viewModel.stations2.observe {
            station2Adapter.clear()
            station2Adapter.addAll(it)
        }
        // TODO: observe get stations result, hide progress bar, show error message if needed
    }

}