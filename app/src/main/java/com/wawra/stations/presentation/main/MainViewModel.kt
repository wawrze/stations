package com.wawra.stations.presentation.main

import com.wawra.stations.base.BaseViewModel
import com.wawra.stations.logic.StationRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(var stationRepository: StationRepository) :
    BaseViewModel() {
}