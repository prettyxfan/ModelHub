// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.DistributeServer.Network;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Hearken to InComing Request.
 * Dispatch ComputeServers.
 * Send Response to WenFrontEnd
 *
 */
public class DistributeServerHearken extends WebSocketServer{

    public DistributeServerHearken( int port ) {
        super( new InetSocketAddress( port ) );
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

        System.out.println(s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }
}
