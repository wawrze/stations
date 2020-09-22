package com.wawra.stations.presentation.main

import com.wawra.stations.base.BaseViewModel
import com.wawra.stations.logic.ModelRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(var modelRepository: ModelRepository) : BaseViewModel() {
}