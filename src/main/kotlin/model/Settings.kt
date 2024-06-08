package com.tobiask.model

import com.tobiask.dataStores.DataStore
import com.tobiask.lastWatered
import com.tobiask.plantConfigurationStore
import com.tobiask.settings
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    var Timedinterval: Int,
    var measurementTimer: Int,
    var wateringDurationManually: Int,
    var wateringDurationTimed: Int,
    var threshold: Int,
    var ideal: Int
){
    fun getIntelligentWateringDuration(): Double{
        val diffToIdeal = settings.ideal - DataStore.measurements.lastOrNull()!!.measurement
        val gramsToAdd =  (diffToIdeal / 100) * plantConfigurationStore.config.soilWeight
        val secondsToWater = gramsToAdd / plantConfigurationStore.config.waterPumpGPS
        return secondsToWater;
    }
}