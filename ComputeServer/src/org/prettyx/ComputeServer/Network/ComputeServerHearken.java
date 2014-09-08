// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.ComputeServer.Network;

import net.sf.json.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ComputeServerHearken extends WebSocketServer {

    public ComputeServerHearken(int port) {
        super( new InetSocketAddress( port ) );
        LogUtility.logUtility().log2out("Initializing WebSocket Hearken.");
    }

    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {
        LogUtility.logUtility().log2out("new connection to " + connection.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket connection, int code, String reason, boolean remote) {
        LogUtility.logUtility().log2out("closed " + connection.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conection, String string) {
        handleMessage(conection, string);
    }

    @Override
    public void onError(WebSocket connection, Exception ex) {
        LogUtility.logUtility().log2out("an error occured on connection " + connection.getRemoteSocketAddress()  + ":" + ex);
    }

    protected void handleMessage(WebSocket connection, String text) {

        JSONObject jsonObject = JSONObject.fromObject(text);

        if(jsonObject.size() == 3){
            LogUtility.logUtility().log2out(jsonObject.toString());
            int action = (Integer)jsonObject.get("action");
            String sid = (String)jsonObject.get("sid");
            String data = (String)jsonObject.get("data");

            try {
                switch(action) {
                    case Message.D_C_RUN: {
                        ActionHandler.runModel();
                        System.out.println(action);
                        break;
                    }
                }
            } catch (Exception e) {
                LogUtility.logUtility().log2err(e.getMessage());
            }

        }


    }

    /**
     * A Wrapper for start() method
     * Check & Start Listening at Given Port
     *
     * @return
     *      SUCCESS/FAIL
     */
    public int checkAndStart() {
        if (!isPortAvailable(getPort())) {
            return StatusCodes.FAIL;
        }
        start();
        LogUtility.logUtility().log2out("WebSocket Hearken Started!");
        return StatusCodes.SUCCESS;
    }

    /**
     * Check Port Available
     *
     * @param port
     *          port to test
     * @return
     *      true/false
     */
    private boolean isPortAvailable(int port){
        boolean flag = false;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            flag = true;
            serverSocket.close();

            LogUtility.logUtility().log2out("Port " + port + " is available.");

        } catch (IOException e) {
            LogUtility.logUtility().log2err("Port " + port + " is unavailable. " + e.getMessage());
        }
        return flag;
    }
}
