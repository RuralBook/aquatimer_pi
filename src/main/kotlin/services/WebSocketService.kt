package com.tobiask.services

import com.tobiask.constants.ServerConstants
import com.tobiask.debugUtil
import com.tobiask.webSocketService
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class WebSocketService(address: InetSocketAddress?) : WebSocketServer(address) {

   var messageService: MessageService = MessageService()
    var connectionService: ConnectionService = ConnectionService()
    override fun run() {
        debugUtil.log("[BOOT-INFO] started websocket service")
        super.run()
    }
    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        connectionService.onOpen(conn, handshake)
    }


    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
        connectionService.onClose(conn, code, reason, remote)
    }

    override fun onMessage(conn: WebSocket, message: String) {
        debugUtil.log("received message from " + conn.remoteSocketAddress + ": " + message)

        messageService.onMessageReceived(conn, message)
    }

    override fun onError(conn: WebSocket?, ex: Exception) {
        System.err.println("an error occurred on connection " + conn?.remoteSocketAddress + ":" + ex)
    }

    override fun onStart() {
        debugUtil.log("server started successfully")
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
