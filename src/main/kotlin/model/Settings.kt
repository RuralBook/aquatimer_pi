package com.tobiask.model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    var Timedinterval: Int,
    var measurementTimer: Int,
    var wateringDurationManually: Int,
    var wateringDurationTimed: Int,
    var threshold: Double,
    var ideal: Double
){
    fun getIntelligentWateringDuration(): Int{
        return 5
    }
}