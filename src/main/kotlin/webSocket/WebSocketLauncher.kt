package com.tobiask.webSocket

import com.tobiask.constants.ServerConstants
import com.tobiask.utils.DataUtil
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class WebSocketLauncher(address: InetSocketAddress?) : WebSocketServer(address) {

    private lateinit var messageController: MessageController

    override fun start() {
        super.start()
        messageController = MessageController(this)
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        DataUtil.generateNewMeasurement();
        conn.send("Welcome to the server!" + DataUtil.measurements.toString()) //This method sends a message to the new client
        broadcast("new connection: " + handshake.resourceDescriptor) //This method sends a message to all clients connected
        println("new connection to " + conn.remoteSocketAddress)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        println("closed " + conn.remoteSocketAddress + " with exit code " + code + " additional info: " + reason)
    }

    override fun onMessage(conn: WebSocket, message: String) {
        println("received message from " + conn.remoteSocketAddress + ": " + DataUtil.measurements.toString())
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        System.err.println("an error occurred on connection " + conn?.remoteSocketAddress + ":" + ex)
    }

    override fun onStart() {
        println("server started successfully")
    }


    companion object {
        fun main(){
            val host = ServerConstants.ip
            val port = ServerConstants.port
            val server = WebSocketLauncher(InetSocketAddress(host, port))
            server.run()
        }
    }
}