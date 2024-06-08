package com.tobiask.model

import kotlinx.serialization.Serializable

@Serializable
data class Measurement(
    val time: String,
    var measurement: Double
){
    override fun toString(): String {
        val returnString = "${this.time}@${this.measurement}"
        return returnString
    }
}
fun voltageToMoisture(measurement: Double): Double {
    // Find the correct interval for the voltageInput
    val voltageData = listOf(2.214, 1.714, 1.214, 1.156, 1.043, 0.953, 0.94)
    val moistureData = listOf(0.0, 20.32520325, 40.6504065, 81.30081301, 121.9512195, 162.601626, 203.2520325)
    for (i in 0 until voltageData.size - 1) {
        if (measurement <= voltageData[i] && measurement >= voltageData[i + 1]) {
            // Perform linear interpolation
            val x0 = voltageData[i]
            val x1 = voltageData[i + 1]
            val y0 = moistureData[i]
            val y1 = moistureData[i + 1]
            return y0 + (measurement - x0) * (y1 - y0) / (x1 - x0)
        }
    }
    return when {
        measurement > voltageData[0] -> moistureData[0]
        measurement < voltageData[voltageData.size - 1] -> moistureData[moistureData.size - 1]
        else -> throw IllegalArgumentException("Voltage input is out of range")
    }
}
