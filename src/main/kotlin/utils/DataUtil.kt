package com.tobiask.utils

import com.tobiask.model.Measurement
import java.time.LocalTime
import kotlin.random.Random

object DataUtil {
    var measurements = mutableListOf<Measurement>()

    //Debug shit
    fun generateNewMeasurement(){
        val newMeasurement = Measurement(time = LocalTime.now(), measurement = Random.nextInt())
        measurements.add(newMeasurement)
    }
}