package com.tobiask.services

import com.tobiask.debugUtil
import com.tobiask.pi4j


class MeasurementService {
    companion object {
        suspend fun main(){
            debugUtil.log("[BOOT-INFO] started measurement service")
            SensorService.getMeasurementsAndSaveInStore(pi4j)
        }
    }

}