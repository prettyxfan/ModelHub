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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;
import org.prettyx.Common.XMLParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.prettyx.DistributeServer.Users.Users;

/**
 * Hearken to InComing Request.
 * Dispatch ComputeServers.
 * Send Response to WenFrontEnd
 *
 */
public class DistributeServerHearken extends WebSocketServer {


    private Map currentUsers = new ConcurrentHashMap<Users, WebSocketServer>();

    public DistributeServerHearken( int port ) {
        super( new InetSocketAddress( port ) );
        LogUtility.logUtility().log2out("Initializing WebSocket Hearken.");
    }

    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {
        System.out.println("new connection to " + connection.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket connection, int code, String reason, boolean remote) {
        System.out.println("closed " + connection.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conection, String string) {

//        System.out.println(s);
//        if(s.equals("Get Model")) {
//            String ModelInfomation = "<components><component><componentName>gopher</componentName>"+
//                    "<componentDescription>It's a long long story</componentDescription>"+
//                    "<inputs><input><inputName>in1</inputName><inputType>string</inputType></input>"+
//                    "<input><inputName>in2</inputName><inputType>string</inputType></input></inputs>"+
//                    "<outputs><output><outputName>out</outputName><outputType>integer</outputType>"+
//                    "</output></outputs><parameters><parameter><parameterName></parameterName><parameterType>"+
//                    "</parameterType></parameter></parameters></component><component><componentName>Hello World"+
//                    "</componentName><componentDescription>The component just prints out the message."+
//                    "</componentDescription><inputs><input><inputName>message</inputName>"+
//                    "<inputType>string</inputType></input></inputs><outputs><output><outputName>out</outputName>"+
//                    "<outputType>string</outputType></output></outputs><parameters><parameter><parameterName>"+
//                    "</parameterName><parameterType></parameterType></parameter></parameters></component></components>";
//            sendToAll(ModelInfomation);
//        }

        LogUtility.logUtility().log2out(conection.getRemoteSocketAddress().toString());

        try {
            Map amap = XMLParser.parserXmlFromString(string);
            LogUtility.logUtility().log2out(amap.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onError(WebSocket connection, Exception ex) {
        System.err.println("an error occured on connection " + connection.getRemoteSocketAddress()  + ":" + ex);
    }

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text
     *            The String to send across the network.
     * @throws InterruptedException
     *             When socket related I/O errors occur.
     */
    public void sendToAll( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }

    protected void handleMessage(WebSocket connection, String text) throws Exception {

        Document document = DocumentHelper.parseText(text);
        Element rootElement = document.getRootElement();
        Element actionElement = rootElement.element("action");
        Element ssidElement = rootElement.element("ssid");
        Element dataElement = rootElement.element("data");




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
