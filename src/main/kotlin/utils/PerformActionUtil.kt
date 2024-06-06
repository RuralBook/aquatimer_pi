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
//{"actionName":"performAction","payload":{"toggleManualWatering":true},"key":"FLORIAN"}           
class PerformActionUtil {
    companion object {
        fun waterPump(action: String){
            debugUtil.log(action)
            when(action.trim('"')){
                "true" -> {
                    if(ModeStore.mode == Mode.MANUALLY) {
                        waterPumpService.shouldWater = true;
                        val responseData = JsonObject(buildMap{
                            put("performingAction", Json.encodeToJsonElement("performingWatering"))
                        })
                        val response = JsonFactory.createFullMessage("publishAction", responseData)
                        webSocketService.broadcast(response)
                    } else {
                        createErrMessage("the current mode isn't set to manual")
                    }
                }
                "false" -> {
                    if(ModeStore.mode == Mode.MANUALLY) {
                        waterPumpService.shouldStop = true;
                        val responseData = JsonObject(buildMap{
                            put("performingAction", Json.encodeToJsonElement("stopping watering"))
                        })
                        val response = JsonFactory.createFullMessage("publishAction", responseData)
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