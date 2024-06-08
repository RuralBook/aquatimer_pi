package com.tobiask.utils

import Message
import com.tobiask.settings
import com.tobiask.dataStores.DataStore
import com.tobiask.ModeStore
import kotlinx.serialization.json.*

class JsonFactory {
    companion object {
        fun createErrMessage(message: String): String{
            return createFullMessage("showErr", JsonObject(
                buildMap {
                    put("message", Json.encodeToJsonElement(message))
                }
            ))
        }
        fun createMessage(key: String, message: String): JsonObject{
            return JsonObject(
                buildMap {
                    put(key, Json.encodeToJsonElement(message))
                }
            )
        }

        fun createFullMessage(actionName: String,payload: JsonObject): String{
            return Json.encodeToJsonElement(
                Message(
                    actionName = actionName,
                    payload = payload,
                    key = ""
                )
            ).toString()
        }

        fun createDataBroadcast(): String{
            return Json.encodeToJsonElement(Message(
                actionName = "publishMeasurement",
                payload = createMessage("data", DataStore.measurements.toString()),
                key = ""
            )).toString()
        }

        fun createInitialDataMessage(): String{
            return createFullMessage("initialData", JsonObject(
                buildMap {
                    put("data", Json.encodeToJsonElement(DataStore.measurements.toString()))
                    put("mode", Json.encodeToJsonElement(ModeStore.mode.toString()))
                    put("settings", Json.encodeToJsonElement(settings))
                }
            ))
        }
    }
}