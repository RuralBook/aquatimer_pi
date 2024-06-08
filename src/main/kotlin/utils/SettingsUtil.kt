package com.tobiask.utils

import com.tobiask.settings
import com.tobiask.debugUtil
import com.tobiask.webSocketService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

class SettingsUtil {
    companion object {
        fun setIntervalTimer(command: String){
            try {
                val newDuration = command.trim('"').toInt()
                settings.Timedinterval = newDuration;
            } catch (e: Exception) {
                debugUtil.log(e.toString())
            }
        }
        fun setMeasurementTimer(command: String){
            try {
                val measurementTimer = command.trim('"').toInt()
                settings.measurementTimer = measurementTimer
            } catch (e: Exception) {
                debugUtil.log(e.toString())
            }
        }
        fun setWateringDurationManually(command: String){
            try {
                val newDuration = command.trim('"').toInt()
                settings.wateringDurationManually = newDuration;
            } catch (e: Exception) {
                debugUtil.log(e.toString())
            }
        }
        fun setWateringDurationTimed(command: String){
            try {
                val newDuration = command.trim('"').toInt()
                settings.wateringDurationTimed = newDuration;
            } catch (e: Exception) {
                debugUtil.log(e.toString())
            }
        }
        fun setNewIdeal(passedIdeal: String){
            try {
                val newIdeal = passedIdeal.trim('"').toInt()
                settings.ideal = newIdeal
            }catch (e: Exception){
                debugUtil.log(e.toString())
            }
        }
        fun setNewThreshold(passedThreshold: String){
            try {
                val newThreshold = passedThreshold.trim('"').toInt()
                settings.ideal = newThreshold
            }catch (e: Exception){
                debugUtil.log(e.toString())
            }
        }

        ///BROADCAST FUNCTIONS
        ///
        fun broadcastNewTimedModeSettings(){
            val jsonObject = JsonObject(
                buildMap {
                    put("timerInterval", Json.encodeToJsonElement(settings.Timedinterval))

                    put("wateringDurationTimed", Json.encodeToJsonElement(settings.wateringDurationTimed))
                }
            )
            val broadcastMessage = JsonFactory.createFullMessage("publishTimedModeSettings", jsonObject)
            webSocketService.broadcast(broadcastMessage)
        }

        fun broadcastNewManualModeSettings(){
            val jsonObject = JsonObject(
                buildMap {
                    put("wateringDurationManually", Json.encodeToJsonElement(settings.wateringDurationManually))
                }
            )
            val broadcastMessage = JsonFactory.createFullMessage("publishManualModeSettings", jsonObject)
            webSocketService.broadcast(broadcastMessage)
        }

        fun broadcastNewMeasurementTimerSetting(){
            val jsonObject = JsonObject(
                buildMap {
                    put("measurementTimer", Json.encodeToJsonElement(settings.measurementTimer))
                }
            )
            val broadcastMessage = JsonFactory.createFullMessage("publishMeasurementTimerSettings", jsonObject)
            webSocketService.broadcast(broadcastMessage)
        }

        fun broadcastNewIntelligentModeSettings(){
            val jsonObject = JsonObject(
                buildMap {
                    put("threshold", Json.encodeToJsonElement(settings.threshold))

                    put("ideal", Json.encodeToJsonElement(settings.ideal))
                }
            )
            val broadcastMessage = JsonFactory.createFullMessage("publishIntelligentModeSettings", jsonObject)
            webSocketService.broadcast(broadcastMessage)
        }
    }
}