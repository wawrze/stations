package com.wawra.stations.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.wawra.stations.R
import com.wawra.stations.base.BaseActivity
import com.wawra.stations.base.Navigation
import com.wawra.stations.logic.errors.ErrorCodes
import com.wawra.stations.logic.repositories.StationRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main_progress_bar.*
import javax.inject.Inject

class MainActivity : BaseActivity(), Navigation {

    @Inject
    lateinit var stationRepository: StationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        updateDataIfNeeded()
    }

    fun updateDataIfNeeded() {
        activity_main_progress_bar.visibility = View.VISIBLE
        stationRepository.updateDataIfNeeded()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                {
                    activity_main_progress_bar.visibility = View.GONE
                    if (!it) {
                        getNavigationController().navigate(R.id.dialog_data_out_of_date)
                    }
                },
                {
                    activity_main_progress_bar.visibility = View.GONE
                    getNavigationController().navigate(
                        R.id.dialog_error,
                        bundleOf(
                            "message" to getString(
                                R.string.unknown_error, ErrorCodes.UPDATE_DATA_MAIN_ACTIVITY.code
                            )
                        )
                    )
                }
            )
            .addToDisposables()
    }

    override fun getNavigationController() = findNavController(R.id.activity_main_fragment)

}