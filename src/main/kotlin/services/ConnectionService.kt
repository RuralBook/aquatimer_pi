package com.tobiask.services

import com.tobiask.adminDataStore
import com.tobiask.debugUtil
import com.tobiask.utils.JsonFactory
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake

class ConnectionService {
    fun onOpen(conn: WebSocket, handshake: ClientHandshake){
        if(!adminDataStore.bannedIps.contains(conn.remoteSocketAddress.toString())) {
            conn.send(JsonFactory.createInitialDataMessage())
            debugUtil.log("new connection to " + conn.remoteSocketAddress)
            adminDataStore.openConnections++
        } else {
            conn.closeConnection(-5, "you're banned")
        }
    }

    fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        adminDataStore.openConnections--
        debugUtil.log("closed " + conn.remoteSocketAddress + " with exit code " + code + " additional info: " + reason)
    }

}