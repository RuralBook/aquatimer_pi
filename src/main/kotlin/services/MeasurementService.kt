package com.tobiask.services

import com.pi4j.Pi4J
import com.tobiask.services.DataService

class MeasurementService {
    fun getMeasurements(){

    }
    companion object {
        fun main(){
            DataService.generateNewMeasurement();
            val pi4j = Pi4J.newAutoContext()
            println(pi4j.boardInfo().boardModel)
            for (i: Int in 0..10){
                println(i)
            }
        }
    }
}