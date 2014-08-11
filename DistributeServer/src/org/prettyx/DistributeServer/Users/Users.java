package org.prettyx.DistributeServer.Users;

import org.java_websocket.WebSocket;

/**
 * Created by XieFan on 8/8/14.
 */

public class Users {

    private WebSocket connection;
    private String sid;

    public Users(WebSocket conn, String id) {
        connection = conn;
        sid = id;
    }

    public Users(){
        connection = null;
        sid = null;
    }

    public WebSocket getConnection(){
        return connection;
    }

    public void setConnection(WebSocket conn) {
        connection = conn;
    }

    public String getSid(){
        return sid;
    }

    public void setSid(String id) {
        sid = id;
    }

}
