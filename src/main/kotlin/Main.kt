package com.tobiask

import com.tobiask.services.MeasurementService
import com.tobiask.services.WebSocketService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

lateinit var webSocketService: WebSocketService

suspend fun main() {
    coroutineScope {
        //starting WebSocket
        launch {  WebSocketService.launchAndSetInstance() }
        //starting Measurement Handler
        launch { MeasurementService.main(); }
    }
}
