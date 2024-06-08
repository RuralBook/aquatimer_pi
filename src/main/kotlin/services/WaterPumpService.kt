package com.tobiask.services

import com.pi4j.io.gpio.digital.DigitalState
import com.tobiask.*
import com.tobiask.model.Mode
import com.tobiask.model.State
import com.tobiask.dataStores.DataStore
import com.tobiask.dataStores.StateStore
import com.tobiask.utils.JsonFactory
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import java.time.LocalTime

class WaterPumpService {
    var shouldWater = false
    var shouldStop = false

    suspend fun mainLoop(){
        debugUtil.log("[BOOT-INFO] started water pump service1")
        try{
                while (true) {
                    debugUtil.log(shouldWater.toString())
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
                                performWatering(null)
                                debugUtil.log("[ACTION] watering Timed...")
                                shouldStop = false
                                setStateAfterWatering()
                            } else if(lastWatered.plusMinutes(10).isBefore(LocalTime.now())){
                                StateStore.state = State.MEASURING
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
        }catch(e:Exception){
            debugUtil.log(e.toString())
        }
    }

    private fun performWatering(isManually: Boolean?){
        val duration = when(isManually){
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
            while (!shouldStop && LocalTime.now().minusSeconds(duration.toLong()).isBefore(startTime)) {
                output.state(DigitalState.HIGH)
            }
            output.state(DigitalState.LOW)
        val responseData = JsonObject(buildMap{
            put("active", Json.encodeToJsonElement("false"))
        })
        val response = JsonFactory.createFullMessage("publishPumpState", responseData)
        webSocketService.broadcast(response)
    }

    private fun setStateAfterWatering(){
        StateStore.state = State.MEASURING_AFTER_WATERING
    }
}