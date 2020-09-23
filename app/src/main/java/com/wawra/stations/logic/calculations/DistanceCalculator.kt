package com.wawra.stations.logic.calculations

import io.reactivex.Single
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object DistanceCalculator {

    fun calculateDistanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double) = Single
        .fromCallable {
            val theta = lon1 - lon2
            var distance = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) +
                    cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))
            distance = acos(distance)
            distance = rad2deg(distance)
            distance *= 111.2
            distance
        }

    private fun deg2rad(deg: Double) = deg * Math.PI / 180.0

    private fun rad2deg(rad: Double) = rad * 180.0 / Math.PI

}