package com.tobiask.services

import com.tobiask.constants.ServerConstants
import com.tobiask.services.webSocket.MessageService
import com.tobiask.services.webSocketService.ConnectionService
import com.tobiask.services.DataService
import com.tobiask.webSocketService
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class WebSocketService(address: InetSocketAddress?) : WebSocketServer(address) {

    private lateinit var messageService: MessageService
    private lateinit var connectionService: ConnectionService

    override fun start() {
        super.start()
        connectionService = ConnectionService()
        messageService = MessageService()
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        connectionService.onOpen(conn, handshake)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        connectionService.onClose(conn, code, reason, remote)
    }

    override fun onMessage(conn: WebSocket, message: String) {
        messageService.onMessageReceived(conn, message)
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        System.err.println("an error occurred on connection " + conn?.remoteSocketAddress + ":" + ex)
    }

    override fun onStart() {
        println("server started successfully")
    }


    companion object {
        fun launchAndSetInstance(){
            val host = ServerConstants.ip
            val port = ServerConstants.port
            val server = WebSocketService(InetSocketAddress(host, port))
            webSocketService = server
            server.run()
        }
    }
}