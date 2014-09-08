// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.DistributeServer;

import net.sf.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.prettyx.Common.*;
import org.prettyx.DistributeServer.Settings.SettingsCenter;

import java.net.URI;

public class DistributeServer {

    private SettingsCenter settingsCenter;
    public static String absolutePathOfDB = null;
    public static String absolutePathOfRuntimeUsers = null;

    /**
     * Initialize the Application Running Environment
     *
     * @return SUCCESS/FAIL
     */
    private int initialize(){
        // Initialize Settings
        settingsCenter = new SettingsCenter();
        if (settingsCenter.loadSettings() != StatusCodes.SUCCESS) {
            return StatusCodes.FAIL;
        }
        absolutePathOfDB = this.settingsCenter.getSetting("Init", "RuntimeDatabasePath");
        absolutePathOfRuntimeUsers = this.settingsCenter.getSetting("Init", "RuntimeUsersPathPath");

        return StatusCodes.SUCCESS;
    }

    /**
     * The Main Application
     * @param args
     *          Command Line Parameters
     */
    public static void main(String[] args) throws Exception {
//        DistributeServer distributeServer = new DistributeServer();
//        distributeServer.initialize();
//
//        DistributeServerHearken distributeServerHearken = new DistributeServerHearken(Integer.valueOf(distributeServer.settingsCenter.getSetting("Network", "Port")));
//        distributeServerHearken.checkAndStart();

        test0();
    }

    public static void test0() {

        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:8529")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    send(JSONObject.fromObject("{action:10,sid: \"nuughll\",data:\"fail\"}").toString());
                }

                @Override
                public void onMessage(String s) {

                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {

                }
            };
            webSocketClient.connect();
        } catch (Exception e){
            LogUtility.logUtility().log2err(e.getMessage());
        }
    }

}
