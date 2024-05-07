package com.tobiask.services.webSocketService

import com.tobiask.services.DataService
import com.tobiask.webSocketService
import jdk.incubator.vector.ByteVector.broadcast
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake

class ConnectionService {
    fun onOpen(conn: WebSocket, handshake: ClientHandshake){
        DataService.generateNewMeasurement();
        conn.send("Welcome to the server!" + DataService.measurements.toString()) //This method sends a message to the new client
        webSocketService.broadcast("new connection: " + handshake.resourceDescriptor) //This method sends a message to all clients connected
        println("new connection to " + conn.remoteSocketAddress)
    }

    fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        println("closed " + conn.remoteSocketAddress + " with exit code " + code + " additional info: " + reason)
    }

}