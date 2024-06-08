package com.tobiask.utils

import Message
import com.tobiask.debugUtil
import com.tobiask.model.Mode
import com.tobiask.ModeStore
import com.tobiask.utils.JsonFactory.Companion.createErrMessage
import com.tobiask.utils.JsonFactory.Companion.createFullMessage
import com.tobiask.utils.JsonFactory.Companion.createMessage
import com.tobiask.waterPumpService
import com.tobiask.webSocketService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.java_websocket.WebSocket
import javax.print.attribute.standard.JobState

class ModeUtil {
    companion object{
        fun setNewMode(newMode: String, conn: WebSocket){
            when(newMode.trim('"')){
                "TIMED" -> {
                    ModeStore.mode = Mode.TIMED
                }
                "INTELLIGENT" -> {
                    ModeStore.mode = Mode.INTELLIGENT
                }
                "MANUALLY" -> {
                    ModeStore.mode = Mode.MANUALLY
                }
                else -> {
                    conn.send(createErrMessage("invalid mode command"))
                }
            }
                debugUtil.log("current Mode is ${ModeStore.mode}")
        }
    }
}