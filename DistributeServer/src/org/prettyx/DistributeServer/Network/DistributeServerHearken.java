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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.prettyx.Common.DBOP;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;
import org.prettyx.Common.XMLParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.prettyx.DistributeServer.Users.Users;

/**
 * Hearken to InComing Request.
 * Dispatch ComputeServers.
 * Send Response to WenFrontEnd
 *
 */
public class DistributeServerHearken extends WebSocketServer {

    public static final int LOGIN = 0;
    public static final int LOGOUT = 1;
    public static final int SIGN_UP = 2;
    public static final int GET_MODEL = 3;
    public static final int RUN = 4;

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
//        System.out.println(string);

//        LogUtility.logUtility().log2out(string);
        handleMessage(conection, string);

    }

    @Override
    public void onError(WebSocket connection, Exception ex) {
        System.err.println("an error occured on connection " + connection.getRemoteSocketAddress()  + ":" + ex);
    }

    protected void handleMessage(WebSocket connection, String text) {

        JSONObject jsonObject = JSONObject.fromObject(text);
        System.out.println(jsonObject.size());
        if(jsonObject.size() == 3){
            LogUtility.logUtility().log2out(jsonObject.toString());
            int action = (Integer)jsonObject.get("action");
            String sid = (String)jsonObject.get("sid");
            String data = (String)jsonObject.get("data");

            try {
                switch(action) {
                    case LOGIN: login(connection, data); break;
                    case LOGOUT: logOut(); break;
                    case SIGN_UP: signUp(); break;
                    case GET_MODEL: getModel(); break;
                    case RUN: runModel(); break;
                    default: LogUtility.logUtility().log2err("action type error");
                }
            } catch (Exception e) {

            }

        }


    }
    /**
     * @TODO
     */

    protected void login(WebSocket connection, String data) throws Exception {

        String sid = UUID.randomUUID().toString();
        Users user = new Users(connection, sid);
        Map userInfo = XMLParser.parserXmlFromString(data);
        String userNameOrEmail = (String)userInfo.get("/message/username_email");
        String password = (String)userInfo.get("/message/password");
        LogUtility.logUtility().log2out("user: "+userNameOrEmail+"password: "+password);

        DBOP dbop = new DBOP();

        Connection connectionToSql = dbop.getConnection();
        PreparedStatement prep = connectionToSql.prepareStatement(
                "select count(*) as rowCount from Users where( username = ? or nickname = ?) and password = ?;");
        prep.setString(1, userNameOrEmail);
        prep.setString(2, userNameOrEmail);
        prep.setString(3, password);

        ResultSet resultSet = prep.executeQuery();

        if (resultSet.getInt("rowCount") == 0) {
            //用户不存在
            JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:0,message:'fail'}");
             connection.send(jsonObject.toString());
        } else {
            JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:1,message:'ok'}");
            connection.send(jsonObject.toString());
        }


    }
    /**
     * @TODO
     */

    protected void logOut() {

    }
    /**
     * @TODO
     */

    protected void signUp() {

    }
    /**
     * @TODO
     */

    protected void getModel() {

    }
    /**
     * @TODO
     */

    protected void runModel() {

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
