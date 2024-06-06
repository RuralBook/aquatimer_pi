package com.tobiask

import com.pi4j.Pi4J
import com.tobiask.dataStores.ModeStore
import com.pi4j.context.Context
import com.pi4j.ktx.io.digital.digitalOutput
import com.pi4j.ktx.io.digital.piGpioProvider
import com.tobiask.dataStores.AdminDataStore
import com.tobiask.dataStores.PlantConfigurationStore
import com.tobiask.model.PlantConfiguration
import com.tobiask.services.MeasurementService
import com.tobiask.services.WebSocketService
import com.tobiask.services.WaterPumpService
import com.tobiask.utils.DebugUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalTime
lateinit var webSocketService: WebSocketService
val plantConfigurationStore = PlantConfigurationStore()
val adminDataStore = AdminDataStore()
val ModeStore = ModeStore();
val waterPumpService = WaterPumpService()

var lastWatered: LocalTime = LocalTime.now()

val pi4j: Context = Pi4J.newAutoContext()
val output = pi4j.digitalOutput(17) {
    piGpioProvider();
}
val debugUtil = DebugUtil()

suspend fun main() {
    debugUtil.init();
    coroutineScope {
        launch { WebSocketService.launchAndSetInstance() }

        launch { MeasurementService.main(); }

        launch { waterPumpService.mainLoop() }
    }
}
