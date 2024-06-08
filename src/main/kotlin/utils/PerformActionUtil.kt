package com.tobiask.utils

import com.tobiask.debugUtil
import com.tobiask.model.Mode
import com.tobiask.ModeStore
import com.tobiask.utils.JsonFactory.Companion.createErrMessage
import com.tobiask.utils.JsonFactory.Companion.createMessage
import com.tobiask.waterPumpService
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.Json
import com.tobiask.webSocketService

class PerformActionUtil {
    companion object {
        fun waterPump(action: String){
            when(action.trim('"')){
                "true" -> {
                    debugUtil.log(action)                  
                    if(ModeStore.mode == Mode.MANUALLY) {
                        waterPumpService.shouldWater = true;
                        val responseData = JsonObject(buildMap{
                            put("active", Json.encodeToJsonElement("true"))
                        })
                        val response = JsonFactory.createFullMessage("publishPumpState", responseData)
                        webSocketService.broadcast(response)
                    } else {
                        createErrMessage("the current mode isn't set to manual")
                    }
                }
                "false" -> {
                    debugUtil.log(action)
                    if(ModeStore.mode == Mode.MANUALLY) {
                        waterPumpService.shouldStop = true;
                        val responseData = JsonObject(buildMap{
                            put("active", Json.encodeToJsonElement("false"))
                        })
                        val response = JsonFactory.createFullMessage("publishPumpState", responseData)
                        webSocketService.broadcast(response)
                    } else {
                        createErrMessage("the current mode isn't set to manual")
                    }
                }
                else -> {
                    createErrMessage("invalid mode command")
                }
            }
        }
    }
}