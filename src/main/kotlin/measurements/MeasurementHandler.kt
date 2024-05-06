package com.tobiask.measurement

import com.pi4j.Pi4J
import com.tobiask.utils.DataUtil

class MeasurementHandler {
    fun getMeasurements(){

    }
    companion object {
        fun main(){
            DataUtil.generateNewMeasurement();
            val pi4j = Pi4J.newAutoContext()
            println(pi4j.boardInfo().boardModel)
            for (i: Int in 0..10){
                println(i)
            }
        }
    }
}