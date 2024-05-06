package com.tobiask

import com.tobiask.measurement.MeasurementHandler
import com.tobiask.webSocket.WebSocketLauncher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main() {
    coroutineScope {
        //starting WebSocket
        launch {  WebSocketLauncher.main() }
        //starting Measurement Handler
        launch { MeasurementHandler.main(); }
    }
}
