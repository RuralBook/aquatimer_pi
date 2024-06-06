package com.tobiask.utils

import com.tobiask.services.WaterPumpService
import com.tobiask.utils.JsonFactory.Companion.createErrMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.java_websocket.WebSocket
import com.tobiask.ModeStore
import com.tobiask.adminDataStore
import com.tobiask.webSocketService


class MessageUtils {
    companion object {
        fun modeCommands(payload: JsonObject){
            if(payload["setMode"] !=null){
                ModeUtil.setNewMode(payload["setMode"].toString())
                val answerData = JsonObject(buildMap{
                    put("currentMode", Json.encodeToJsonElement(ModeStore.mode))
                })
                val answer = JsonFactory.createFullMessage("broadcastedMode", answerData)
                webSocketService.broadcast(answer)
            } else {
                val errResponse = createErrMessage("invalid command")
            }
        }
        fun dataCommands(payload: JsonObject): JsonObject{
            return if(payload["getData"] != null){
                DataUtil.getData(payload["getData"].toString())
            } else {
                createErrMessage("invalid command")
            }
        }
        fun setMeasurementInterval(payload: JsonObject){
            val setMeasurementInterval = "setMeasurementInteval"
            if(payload[setMeasurementInterval] != null){
                SettingsUtil.setMeasurementTimer(payload[setMeasurementInterval].toString())
            }
        }

        fun setManualModeSettings(payload: JsonObject){
            val setWateringDurationManually = "setWateringDurationManually"
            if (payload[setWateringDurationManually] != null){
                SettingsUtil.setWateringDurationManually(payload[setWateringDurationManually].toString())
            }
            SettingsUtil.broadcastNewManualModeSettings()
        }

        fun setTimerModeSettings(payload: JsonObject){
            val setTimedInterval =  "setTimedInterval"

            val setWateringDurationTimed = "setWateringDurationTimed"

            if(payload[setTimedInterval] != null &&

                payload[setWateringDurationTimed] != null
            ){
                SettingsUtil.setIntervalTimer(payload[setTimedInterval].toString())
                SettingsUtil.setWateringDurationTimed(payload[setWateringDurationTimed].toString())
            }
            SettingsUtil.broadcastNewTimedModeSettings()
        }

        fun setIntelligentModeSettings(payload: JsonObject){
            val newIdealValue = "idealValue"
            val newThreshold = "threshold"
            if(payload[newThreshold] != null && payload[newIdealValue] != null){
                SettingsUtil.setNewThreshold(payload[newThreshold].toString())
                SettingsUtil.setNewIdeal(payload[newIdealValue].toString())
                SettingsUtil.broadcastNewIntelligentModeSettings()
            }
        }

        fun performAction(payload: JsonObject){
            if (payload["toggleManualWatering"] != null){
                PerformActionUtil.waterPump(payload["toggleManualWatering"].toString());
            } else {
                val errResponse = createErrMessage("invalid command")
            }
        }

        fun configPlantData(payload: JsonObject){
            val plantSoilMass = "soilMass"
            val plantWaterPumpGPS = "waterPumpGPS"

            if(payload[plantSoilMass] != null && payload[plantWaterPumpGPS] != null){
                PlantConfigurationUtil.setSoilWeight(payload[plantSoilMass].toString())
                PlantConfigurationUtil.setWaterPumpGPS(payload[plantWaterPumpGPS].toString())

                PlantConfigurationUtil.broadcastNewConfiguration()
            }
        }

        fun banIP(payload: JsonObject){
            if(payload["ip"] != null){
                val ip = payload["ip"].toString()
                adminDataStore.bannedIps.add(ip)
            }
        }
    }
}