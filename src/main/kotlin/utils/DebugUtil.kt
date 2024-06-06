package com.tobiask.utils

import com.pi4j.ktx.printLoadedPlatforms
import com.pi4j.ktx.printProviders
import com.pi4j.ktx.printRegistry
import com.pi4j.util.Console
import com.tobiask.pi4j

class DebugUtil {
        val console = Console().title("Moisture Software")
        fun init(){
            console.printProviders(pi4j = pi4j)
            console.printLoadedPlatforms(pi4j = pi4j)
            console.printRegistry(pi4j)
        }
        fun log(message:String){
            console.println("[MESSEKT : LOG] -> ${message}")
        }
}