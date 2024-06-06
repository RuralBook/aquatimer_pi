package com.tobiask.dataStores

import com.pi4j.Pi4J
import com.pi4j.context.Context
import com.pi4j.ktx.pi4j
import com.tobiask.model.Measurement
import java.time.LocalTime
import kotlin.random.Random

object DataStore {
    var measurements = mutableListOf<Measurement>()
    var tempMeasurements = 0.0;
    var divider = 0;

    fun reset(){
        divider = 0;
        tempMeasurements = 0.0;
    }
}