package com.tobiask.utils

import com.tobiask.debugUtil
import com.tobiask.plantConfigurationStore
import com.tobiask.webSocketService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

class PlantConfigurationUtil {
    companion object {
        fun setSoilWeight(weight: String){
            try {
                val newWeight = weight.trim('"').toDouble()
                plantConfigurationStore.config.soilWeight = newWeight
            }catch (e: Exception){
                debugUtil.log(e.toString())
            }
        }
        fun setWaterPumpGPS(waterPumpGPS: String){
            try {
                val waterPumpNewGPS = waterPumpGPS.trim('"').toDouble()
                plantConfigurationStore.config.waterPumpGPS = waterPumpNewGPS
            }catch (e: Exception){
                debugUtil.log(e.toString())
            }
        }
        fun broadcastNewConfiguration(){
            val jsonObject = JsonObject(
                buildMap {
                    put("soilWeight", Json.encodeToJsonElement(plantConfigurationStore.config.soilWeight))

                    put("waterPumpGPS", Json.encodeToJsonElement(plantConfigurationStore.config.waterPumpGPS))
                }
            )
            val broadcastMessage = JsonFactory.createFullMessage("publishPlantConfiguration", jsonObject)
            webSocketService.broadcast(broadcastMessage)
        }
    }
}