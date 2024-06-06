package com.tobiask.services

import com.pi4j.context.Context
import com.tobiask.adminDataStore
import com.tobiask.settings
import com.tobiask.debugUtil
import com.tobiask.model.Measurement
import com.tobiask.dataStores.DataStore
import com.tobiask.utils.JsonFactory
import com.tobiask.webSocketService
import kotlinx.coroutines.delay
import one.microproject.rpi.hardware.gpio.sensors.ADS1115Builder
import java.time.LocalTime


class SensorService {
    companion object{
        suspend fun getMeasurementsAndSaveInStore(context: Context){
                ADS1115Builder.get().context(context).build().use { ads1115 ->
                    while (true) {
                        val startTime = LocalTime.now();
                        while (LocalTime.now().minusMinutes(settings.measurementTimer.toLong()).isBefore(startTime)) {
                            DataStore.divider++
                            val aIn0: Double = ads1115.aIn0
                            DataStore.tempMeasurements += aIn0;
                            debugUtil.log("    [MEASUREMENT DATA] $aIn0 (V) And ${adminDataStore.openConnections} open connections")
                            delay(5000)
                        }
                        debugUtil.log("[MEASUREMENT INFO] Added AVG to List (SUM: ${DataStore.tempMeasurements} | DIVIDER: ${DataStore.divider})")
                        val newMeasurement = Measurement(
                            time = LocalTime.now().toString(),
                            measurement = DataStore.tempMeasurements/ DataStore.divider
                        )
                        DataStore.measurements.add(newMeasurement)
                        DataStore.reset();
                        webSocketService.broadcast(JsonFactory.createDataBroadcast())
                    }
                }
        }
    }
}