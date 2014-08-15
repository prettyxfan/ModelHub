// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.DistributeServer.Network;

import net.sf.json.JSONObject;
import org.java_websocket.WebSocket;
import org.prettyx.Common.DBOP;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.XMLParser;
import org.prettyx.DistributeServer.Modeling.XMLCreater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

/**
 * Action Handler
 * handle the message having different action
 * action is dived into login, logou, sign up, get model, run model
 *
 */

public class ActionHandler {

    /**
     * Action when user using userName or email to login
     *
     * @param connection, data
     *
     */
    public static void userNameOrEmailTologin(WebSocket connection, String data) throws Exception {
        System.out.println("here1");

        String sid = UUID.randomUUID().toString();
        Map userInfo = XMLParser.parserXmlFromString(data);
        String userNameOrEmail = (String)userInfo.get("/message/username_email");
        String password = (String)userInfo.get("/message/password");
        String remember = (String)userInfo.get("/message/remember");

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
            System.out.println("fail1");
            prep = connectionToSql.prepareStatement(
                    "select count(*) as rowCount from Users where username = ? or nickname = ?;");
            prep.setString(1, userNameOrEmail);
            prep.setString(2, userNameOrEmail);

            resultSet = prep.executeQuery();
            if (resultSet.getInt("rowCount") == 0) {
                JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:0,message:''}");
                connection.send(jsonObject.toString());
            } else {
                JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:1,message:''}");
                connection.send(jsonObject.toString());
            }
        } else {
            System.out.println("ok1");
            JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:2,message:'" + sid + "'}");
            connection.send(jsonObject.toString());
            String token = null;
            if (remember.equals("true")){
                token = sid;
            } else {
                token = null;
            }
            prep = connectionToSql.prepareStatement(
                    "UPDATE Users SET token=? where ( username = ? or nickname = ?) and password = ?;");
            prep.setString(1, token);
            prep.setString(2, userNameOrEmail);
            prep.setString(3, userNameOrEmail);
            prep.setString(4, password);
            prep.executeUpdate();

            prep = connectionToSql.prepareStatement(
                    "select sid  from Users where username = ? or nickname = ?;");
            prep.setString(1, userNameOrEmail);
            prep.setString(2, userNameOrEmail);

            resultSet = prep.executeQuery();
            String user = resultSet.getString("sid");
            System.out.println("user = " + user);
            DistributeServerHearken.currentUsers.put(connection, user);
        }
        prep.close();
        connectionToSql.close();
        resultSet.close();

    }

    /**
     * Action when user using sid to login
     *
     * @param connection, sid
     *
     */
    public static void userSidTologin(WebSocket connection, String sid) throws Exception {
        System.out.println("here2");
        System.out.println("sid = "+sid);
        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep = connectionToSql.prepareStatement(
                "select count(*) as rowCount from Users where token ='"+ sid +"';");
        ResultSet resultSet = prep.executeQuery();

        System.out.println("rowCount = "+resultSet.getInt("rowCount"));

        if (resultSet.getInt("rowCount") == 0) {
            //用户不存在
            System.out.println("fail2");
            JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:3,message:'fail'}");
            connection.send(jsonObject.toString());
        } else {
            System.out.println("ok2");
            JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:4,message:'ok'}");
            connection.send(jsonObject.toString());

            prep = connectionToSql.prepareStatement(
                    "select sid  from Users where token = ?;");
            prep.setString(1, sid);
            resultSet = prep.executeQuery();
            String user = resultSet.getString("sid");
            System.out.println("user = " + user);
            DistributeServerHearken.currentUsers.put(connection, user);
        }
        prep.close();
        connectionToSql.close();
        resultSet.close();

    }
    /**
     * @TODO
     */

    public static void logOut() {

    }
    /**
     * @TODO
     */
    /**
     * Action when user sign up
     *
     * @param connection, data
     *
     */
    public static void signUp(WebSocket connection, String data) throws Exception{
        System.out.println("here3");

        String sid = UUID.randomUUID().toString();
        Map userInfo = XMLParser.parserXmlFromString(data);
        String userName = (String)userInfo.get("/message/userName");
        String email = (String)userInfo.get("/message/userEmail");
        String password = (String)userInfo.get("/message/password");
        LogUtility.logUtility().log2out("username: "+userName+" email: "+email+" password: "+password);

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep = connectionToSql.prepareStatement(  //email has been sign up
                "select count(*) as rowCount from Users where username = ?;");
        prep.setString(1, email);
        ResultSet resultSet = prep.executeQuery();
        if(resultSet.getInt("rowCount") != 0){
            System.out.println("fail3");
            JSONObject jsonObject = JSONObject.fromObject("{action:'sign_up',StatusCode:5,message:'This Email has been sign up'}");
            connection.send(jsonObject.toString());

        } else {

            prep = connectionToSql.prepareStatement(
                    "select count(*) as rowCount from Users where nickname = ?;");
            prep.setString(1, userName);

            resultSet = prep.executeQuery();

            if (resultSet.getInt("rowCount") != 0) { //username has been sign up
                System.out.println("fail4");
                JSONObject jsonObject = JSONObject.fromObject("{action:'sign_up',StatusCode:6,message:'This UserName has been sign up'}");
                connection.send(jsonObject.toString());
            } else {
                System.out.println("ok4");
                prep = connectionToSql.prepareStatement("insert into Users values(?,?,?,null,?) ;");
                prep.setString(1, email);
                prep.setString(2, userName);
                prep.setString(3,password);
                String user = UUID.randomUUID().toString();
                prep.setString(4, user);
                prep.executeUpdate();
                JSONObject jsonObject = JSONObject.fromObject("{action:'sign_up',StatusCode:7,message:'success'}");
                connection.send(jsonObject.toString());
            }
        }
        prep.close();
        connectionToSql.close();
        resultSet.close();
    }
    /**
     * @TODO
     */

    public static void getModel(WebSocket connection) throws SQLException {
        String userID = (String)DistributeServerHearken.currentUsers.get(connection);

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep = connectionToSql.prepareStatement(  //email has been sign up
                "select description from Models where owner = ?;");
        prep.setString(1, userID);
        ResultSet resultSet = prep.executeQuery();
        String modelDescription = "";
        while (resultSet.next()) {
            modelDescription += resultSet.getString("description");
        }
        modelDescription = XMLCreater.ComponentXML(modelDescription);
        JSONObject jsonObject = JSONObject.fromObject("{action:'get model',StatusCode:0,message:\""+modelDescription+"\"}");
        connection.send(jsonObject.toString());

        System.out.println(jsonObject.toString());

    }
    /**
     * @TODO
     */

    public static void runModel() {

    }
}
