package com.tobiask.utils

import com.tobiask.debugUtil
import com.tobiask.dataStores.DataStore
import com.tobiask.ModeStore
import kotlinx.serialization.json.JsonObject

class DataUtil {
    companion object {
        fun getData(command: String){
            debugUtil.log("passed command  =  $command")
            when(command.trim('"') ){
                "measurements" -> {
                    debugUtil.log(DataStore.measurements.toString())
                    JsonFactory.createMessage("measurement", DataStore.measurements.toString())
                }
                "getCurrentMode" -> {
                    debugUtil.log(ModeStore.mode.toString())
                    JsonFactory.createMessage("mode", ModeStore.mode.toString())
                }
                else -> {
                    JsonFactory.createErrMessage("invalid data command")
                }
            }
        }
    }
}