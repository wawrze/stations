package com.wawra.stations.presentation

import android.os.Bundle
import androidx.navigation.findNavController
import com.wawra.stations.R
import com.wawra.stations.base.BaseActivity
import com.wawra.stations.base.Navigation
import com.wawra.stations.logic.repositories.StationRepository
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
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
        // TODO: show progress bar
        stationRepository.updateDataIfNeeded()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                {
                    // TODO: hide progress bar
                    if (!it) {
                        // TODO: show error message
                    }
                },
                {
                    // TODO: hide progress bar
                    // TODO: show error message
                }
            )
            .addToDisposables()
    }

    override fun getNavigationController() = findNavController(R.id.activity_main_fragment)

}