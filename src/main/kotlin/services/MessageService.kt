package com.tobiask.services


import com.tobiask.utils.JsonFactory
import org.java_websocket.WebSocket
import kotlinx.serialization.json.*
import Message
import com.tobiask.key
import com.tobiask.utils.MessageUtils

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
            "configurePlantData"->{
                MessageUtils.configPlantData(message.payload)
            }
            "changeIntelligentModeSettings"->{
                if(message.key == key) {
                    MessageUtils.setIntelligentModeSettings(message.payload)
                } else {
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "changeTimedModeSettings" -> {
                if(message.key == key) {
                    MessageUtils.setTimerModeSettings(message.payload)
                } else {
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
            "changeMeasurementInterval" -> {
                if(message.key == key) {
                    MessageUtils.setMeasurementInterval(message.payload)
                } else {
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "mode" -> {
                if(message.key == key) {
                    MessageUtils.modeCommands(message.payload)
                } else {
                    val errResponse = JsonFactory.createErrMessage("Only SuperUser can change such Settings")
                    conn.send(errResponse.toString())
                }
            }
            "data" -> {
                MessageUtils.dataCommands(message.payload)
            }
            "performAction" -> {
                if(message.key == key){
                    MessageUtils.performAction(message.payload)
                }
            }
            else -> {

            }
        }
    }
}