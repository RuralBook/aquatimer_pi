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
import kotlinx.serialization.json.JsonElement
import javax.swing.text.Element


class MessageUtils {
    companion object {
        fun modeCommands(payload: JsonObject, conn: WebSocket){
            if(payload["newModeId"] !=null){
                ModeUtil.setNewMode(payload["newModeId"].toString(), conn)
                val answerData = JsonObject(buildMap{
                    put("currentMode", Json.encodeToJsonElement(ModeStore.mode))
                })
                val answer = JsonFactory.createFullMessage("publishMode", answerData)
                webSocketService.broadcast(answer)
            } else {
                val errResponse = createErrMessage("invalid command")
            }
        }
        fun dataCommands(payload: JsonObject) {
            if(payload["getData"] != null){
                DataUtil.getData(payload["getData"].toString())
            } else {
                createErrMessage("invalid command")
            }
        }
        fun setMeasurementInterval(payload: JsonObject){
            val setMeasurementInterval = "MeasurementIntervalMinutes"
            if(payload[setMeasurementInterval] != null){
                SettingsUtil.setMeasurementTimer(payload[setMeasurementInterval].toString())
            }
            SettingsUtil.broadcastNewMeasurementTimerSetting()
        }

        fun setManualModeSettings(payload: JsonObject){
            val setWateringDurationManually = "manualWateringDuration"
            if (payload[setWateringDurationManually] != null){
                SettingsUtil.setWateringDurationManually(payload[setWateringDurationManually].toString())
            }
            SettingsUtil.broadcastNewManualModeSettings()
        }

        fun setTimerModeSettings(payload: JsonObject){
            val setTimedInterval =  "timerInterval"
            val setWateringDurationTimed = "timerWateringDuration"

            if(payload[setTimedInterval] != null &&
                payload[setWateringDurationTimed] != null
            ){
                SettingsUtil.setIntervalTimer(payload[setTimedInterval].toString())
                SettingsUtil.setWateringDurationTimed(payload[setWateringDurationTimed].toString())
            }
            SettingsUtil.broadcastNewTimedModeSettings()
        }

        fun setIntelligentModeSettings(payload: JsonObject){
            val newIdealValue = "ideal"
            val newThreshold = "threshold"
            if(payload[newThreshold] != null && payload[newIdealValue] != null){
                SettingsUtil.setNewThreshold(payload[newThreshold].toString())
                SettingsUtil.setNewIdeal(payload[newIdealValue].toString())
                SettingsUtil.broadcastNewIntelligentModeSettings()
            }
        }

        fun performAction(payload: JsonObject){
            if (payload["active"] != null){
                PerformActionUtil.waterPump(payload["active"].toString());
            } else {

                val errResponse = createErrMessage("invalid command")
            }
        }

        fun configPlantData(payload: JsonObject){
            val plantSoilMass = "soilWeightG"
            val plantWaterPumpGPS = "flowRateGPS"

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