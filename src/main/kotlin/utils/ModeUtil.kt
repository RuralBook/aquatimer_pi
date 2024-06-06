package com.tobiask.utils

import Message
import com.tobiask.debugUtil
import com.tobiask.model.Mode
import com.tobiask.ModeStore
import com.tobiask.utils.JsonFactory.Companion.createErrMessage
import com.tobiask.utils.JsonFactory.Companion.createMessage
import com.tobiask.waterPumpService
import com.tobiask.webSocketService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

class ModeUtil {
    companion object{
        fun setNewMode(newMode: String){
            var actionName = "newMode"
            val jsonObject = when(newMode.trim('"')){
                "TIMED" -> {
                    ModeStore.mode = Mode.TIMED
                    JsonObject(
                        buildMap {
                            put("mode", Json.encodeToJsonElement(ModeStore.mode))
                        }
                    )
                }
                "INTELLIGENT" -> {
                    ModeStore.mode = Mode.INTELLIGENT
                    JsonObject(
                        buildMap {
                            put("mode", Json.encodeToJsonElement(ModeStore.mode))
                        }
                    )
                }
                "MANUALLY" -> {
                    ModeStore.mode = Mode.MANUALLY
                    JsonObject(
                        buildMap {
                            put("mode", Json.encodeToJsonElement(ModeStore.mode))
                        }
                    )
                }
                else -> {
                    actionName = "ShowErr"
                    createErrMessage("invalid mode command")
                }
            }
            val message = Message(
                    actionName = actionName,
                    payload = jsonObject,
                    key = ""
                )
                
            webSocketService.broadcast(Json.encodeToJsonElement(message).toString())
                debugUtil.log("changed Mode To ${ModeStore.mode}")
        }
    }
}