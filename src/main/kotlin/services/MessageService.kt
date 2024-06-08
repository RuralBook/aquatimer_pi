package com.tobiask.services


import com.tobiask.utils.JsonFactory
import org.java_websocket.WebSocket
import kotlinx.serialization.json.*
import Message
import com.tobiask.ModeStore
import com.tobiask.key
import com.tobiask.settings
import com.tobiask.utils.MessageUtils
import com.tobiask.webSocketService

class MessageService(){
    fun onMessageReceived(conn: WebSocket, message: String){
        val jsonMessage = Json.decodeFromString<Message>(message)
        makeUseOfActionName(jsonMessage, conn)
    }

    private fun makeUseOfActionName(message: Message, conn: WebSocket){
        when(message.actionName){
            "banIP" -> {
                if (message.key == key){
                    MessageUtils.banIP(message.payload)
                }
            }
            "changePlantData"->{
                if(message.key == key) {
                    MessageUtils.configPlantData(message.payload)
                    MessageUtils.setMeasurementInterval(message.payload)
                } else {
                    val jsonObject = JsonObject(
                        buildMap {
                            put("wateringDurationManually", Json.encodeToJsonElement(settings.wateringDurationManually))
                        }
                    )
                    val resetState = JsonFactory.createFullMessage("publishManualModeSettings", jsonObject)
                    conn.send(resetState)
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "changeIntelligentModeSettings"->{
                if(message.key == key) {
                    MessageUtils.setIntelligentModeSettings(message.payload)
                } else {
                    val jsonObject = JsonObject(
                        buildMap {
                            put("threshold", Json.encodeToJsonElement(settings.threshold))

                            put("ideal", Json.encodeToJsonElement(settings.ideal))
                        }
                    )
                    val resetStateMessage = JsonFactory.createFullMessage("publishIntelligentModeSettings", jsonObject)
                    conn.send(resetStateMessage)
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "changeTimedModeSettings" -> {
                if(message.key == key) {
                    MessageUtils.setTimerModeSettings(message.payload)
                } else {
                    val jsonObject = JsonObject(
                        buildMap {
                            put("timerInterval", Json.encodeToJsonElement(settings.Timedinterval))

                            put("wateringDurationTimed", Json.encodeToJsonElement(settings.wateringDurationTimed))
                        }
                    )
                    val resetStateMessage = JsonFactory.createFullMessage("publishTimedModeSettings", jsonObject)
                    conn.send(resetStateMessage)
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "changeManualModeSettings" -> {
                if(message.key == key) {
                    MessageUtils.setManualModeSettings(message.payload)
                } else {
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "changeMode" -> {
                if(message.key == key) {
                    MessageUtils.modeCommands(message.payload, conn)
                } else {
                    val answerData = JsonObject(buildMap{
                        put("currentMode", Json.encodeToJsonElement(ModeStore.mode))
                    })
                    val setStateMessage = JsonFactory.createFullMessage("publishMode", answerData)
                    conn.send(setStateMessage)
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "data" -> {
                //MessageUtils.dataCommands(message.payload)
            }
            "setWaterPumpState" -> {
                if(message.key == key){
                    MessageUtils.performAction(message.payload)
                } else {
                    val responseData = JsonObject(buildMap{
                        put("active", Json.encodeToJsonElement("true"))
                    })
                    val response = JsonFactory.createFullMessage("publishPumpState", responseData)
                    conn.send(response)
                }
            }
            else -> {

            }
        }
    }
}