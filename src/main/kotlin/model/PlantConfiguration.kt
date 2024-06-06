package com.tobiask.model

import kotlinx.serialization.Serializable

@Serializable
data class PlantConfiguration(
    var soilWeight: Double,
    var waterPumpGPS: Double
)
