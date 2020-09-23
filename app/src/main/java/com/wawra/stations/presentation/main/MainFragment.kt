package com.wawra.stations.presentation.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.wawra.stations.R
import com.wawra.stations.base.BaseFragment
import com.wawra.stations.base.ViewModelProviderFactory
import com.wawra.stations.database.entities.Station
import kotlinx.android.synthetic.main.activity_main_progress_bar.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import javax.inject.Inject

class MainFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var station1Adapter: AutoCompleteAdapter
    private lateinit var station2Adapter: AutoCompleteAdapter
    private var selectedStation1: Station? = null // TODO: save on orientation change
    private var selectedStation2: Station? = null // TODO: save on orientation change

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
        setupButtons()
        closeKeyboard()
    }

    override fun onPause() {
        super.onPause()
        closeKeyboard()
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
        val adapter = if (forStation1) station1Adapter else station2Adapter
        view.apply {
            setAdapter(adapter)
            threshold = 2
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showDropDown() else dismissDropDown()
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(cs: CharSequence, s: Int, c: Int, a: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (forStation1) selectedStation1 = null else selectedStation2 = null
                    bindStationsData()
                    if (s.toString().length > 1) {
                        activity_main_progress_bar?.visibility = View.VISIBLE
                        viewModel.getMatchingStations(s.toString(), forStation1)
                    }
                }
            })
            setOnItemClickListener { _, _, position, _ ->
                closeKeyboard()
                val station = adapter.getItem(position)
                if (forStation1) selectedStation1 = station else selectedStation2 = station
                bindStationsData()
            }
        }
    }

    private fun bindStationsData() {
        fragment_main_station_1_name_value.text = selectedStation1?.name.orEmpty()
        fragment_main_station_1_city_value.text = selectedStation1?.city.orEmpty()
        fragment_main_station_1_region_value.text = selectedStation1?.region.orEmpty()
        fragment_main_station_1_country_value.text = selectedStation1?.country.orEmpty()

        fragment_main_station_2_name_value.text = selectedStation2?.name.orEmpty()
        fragment_main_station_2_city_value.text = selectedStation2?.city.orEmpty()
        fragment_main_station_2_region_value.text = selectedStation2?.region.orEmpty()
        fragment_main_station_2_country_value.text = selectedStation2?.country.orEmpty()

        fragment_main_calculate_button.apply {
            if (selectedStation1 != null && selectedStation2 != null) {
                setBackgroundResource(R.drawable.bg_button_enabled)
                context?.let { setTextColor(ContextCompat.getColor(it, R.color.gray_dark)) }
            } else {
                setBackgroundResource(R.drawable.bg_button_disabled)
                context?.let { setTextColor(ContextCompat.getColor(it, R.color.gray_light)) }
                fragment_main_distance_label.visibility = View.GONE
                fragment_main_distance_value.visibility = View.GONE
                fragment_main_station_icon_3.visibility = View.GONE
                fragment_main_station_icon_4.visibility = View.GONE
                fragment_main_station_arrow_left.visibility = View.GONE
                fragment_main_station_arrow_right.visibility = View.GONE
            }
        }
    }

    private fun setupButtons() {
        fragment_main_calculate_button.setOnClickListener {
            if (selectedStation1 != null && selectedStation2 != null) {
                viewModel.getDistance(
                    selectedStation1 ?: return@setOnClickListener,
                    selectedStation2 ?: return@setOnClickListener
                )
            } else {
                navigate?.navigate(
                    MainFragmentDirections.toDialogError(getString(R.string.choose_stations_error))
                )
            }
        }
    }

    private fun setupObservers() {
        viewModel.stations1.observe {
            station1Adapter.clear()
            station1Adapter.addAll(it)
            activity_main_progress_bar?.visibility = View.GONE
        }
        viewModel.stations2.observe {
            station2Adapter.clear()
            station2Adapter.addAll(it)
            activity_main_progress_bar?.visibility = View.GONE
        }
        viewModel.distance.observe {
            fragment_main_distance_value.text = getString(R.string.distance_value, it)
            fragment_main_distance_label.visibility = View.VISIBLE
            fragment_main_distance_value.visibility = View.VISIBLE
            fragment_main_station_icon_3.visibility = View.VISIBLE
            fragment_main_station_icon_4.visibility = View.VISIBLE
            fragment_main_station_arrow_left.visibility = View.VISIBLE
            fragment_main_station_arrow_right.visibility = View.VISIBLE
        }
        viewModel.dataOutOfDateError.observe {
            activity_main_progress_bar?.visibility = View.GONE
            navigate?.navigate(
                MainFragmentDirections.toDialogDataOutOfDate()
            )
        }
        viewModel.unknownError.observe {
            activity_main_progress_bar?.visibility = View.GONE
            navigate?.navigate(
                MainFragmentDirections.toDialogError(getString(R.string.unknown_error, it))
            )
        }
    }

    private fun closeKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

}