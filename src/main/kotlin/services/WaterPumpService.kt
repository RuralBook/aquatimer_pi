package com.tobiask.services

import com.pi4j.io.gpio.digital.DigitalState
import com.tobiask.settings
import com.tobiask.debugUtil
import com.tobiask.lastWatered
import com.tobiask.model.Mode
import com.tobiask.model.State
import com.tobiask.output
import com.tobiask.dataStores.DataStore
import com.tobiask.ModeStore
import com.tobiask.dataStores.StateStore
import kotlinx.coroutines.delay
import java.time.LocalTime

class WaterPumpService {
    var shouldWater = false
    var shouldStop = false

    suspend fun mainLoop(){
        debugUtil.log("[BOOT-INFO] started water pump service")
                while (true) {
                    val currentMode = ModeStore.mode
                    when (currentMode) {
                        Mode.MANUALLY -> {
                            if (shouldWater) {
                                debugUtil.log("[ACTION] watering Manually...")
                                performWatering(true)
                                shouldWater = false
                                shouldStop = false
                                setStateAfterWatering()
                            }
                        }

                        Mode.INTELLIGENT -> {
                            val lastMeasurement = DataStore.measurements.lastOrNull()?.measurement;
                            if (lastMeasurement != null && lastMeasurement > settings.threshold &&
                                StateStore.state != State.MEASURING_AFTER_WATERING
                            ) {
                                performWatering(false)
                                debugUtil.log("[ACTION] watering Timed...")
                                shouldStop = false
                                setStateAfterWatering()
                            } else {
                                //debugUtil.log("[INFO] STATE: ${StateStore.state}, VAL: ${DataStore.measurements.lastOrNull()?.measurement}")
                            }
                        }
                        Mode.TIMED -> {
                            debugUtil.log("Timer -> ${settings.Timedinterval}")
                            if (LocalTime.now() >= lastWatered.plusMinutes(settings.Timedinterval.toLong()*60)) {
                                debugUtil.log("[ACTION] watering Timed...")
                                performWatering(false)
                                shouldStop = false
                                setStateAfterWatering()
                            }
                        }
                    }
                    delay(500)
                }
    }

    private fun performWatering(isManually: Boolean?){
        var duration = when(isManually){
            null -> {
                settings.getIntelligentWateringDuration()
            }
            true -> {
                settings.wateringDurationManually
            }
            false -> {
                settings.wateringDurationTimed
            }
        }
        val startTime = LocalTime.now()
        while(!shouldStop && LocalTime.now().minusSeconds(duration.toLong()).isBefore(startTime)){
            output.state(DigitalState.HIGH)
        }
        output.state(DigitalState.LOW)
    }

    private fun setStateAfterWatering(){
        StateStore.state = State.MEASURING_AFTER_WATERING
    }
}