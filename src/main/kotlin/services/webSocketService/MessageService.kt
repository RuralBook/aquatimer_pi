package com.tobiask.services.webSocket

import com.tobiask.services.DataService
import org.java_websocket.WebSocket

class MessageService(){
    fun onMessageReceived(conn: WebSocket, message: String){
        println("received message from " + conn.remoteSocketAddress + ": " + DataService.measurements.toString())
    }
}