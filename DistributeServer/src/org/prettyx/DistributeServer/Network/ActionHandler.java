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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.java_websocket.WebSocket;
import org.prettyx.Common.*;
import org.prettyx.DistributeServer.DistributeServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
            prep = connectionToSql.prepareStatement(
                    "select count(*) as rowCount from Users where username = ? or nickname = ?;");
            prep.setString(1, userNameOrEmail);
            prep.setString(2, userNameOrEmail);

            resultSet = prep.executeQuery();
            if (resultSet.getInt("rowCount") == 0) {
                LogUtility.logUtility().log2out("fail1, user is not exist");

                JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:0,message:''}");
                connection.send(jsonObject.toString());
            } else {
                LogUtility.logUtility().log2out("fail1, password is not right");

                JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:1,message:''}");
                connection.send(jsonObject.toString());
            }
        } else {
            LogUtility.logUtility().log2out("log in is ok");
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
        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep = connectionToSql.prepareStatement(
                "select count(*) as rowCount from Users where token ='"+ sid +"';");
        ResultSet resultSet = prep.executeQuery();

        if (resultSet.getInt("rowCount") == 0) {
            //用户不存在
            LogUtility.logUtility().log2out("fail, user is not exist");
            JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:3,message:'fail'}");
            connection.send(jsonObject.toString());
        } else {
            LogUtility.logUtility().log2out("log in is ok");
            JSONObject jsonObject = JSONObject.fromObject("{action:'login',StatusCode:4,message:'ok'}");
            connection.send(jsonObject.toString());

            prep = connectionToSql.prepareStatement(
                    "select sid  from Users where token = ?;");
            prep.setString(1, sid);
            resultSet = prep.executeQuery();
            String user = resultSet.getString("sid");
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
     * Action when user sign up
     *
     * @param connection, data
     *
     */
    public static void signUp(WebSocket connection, String data) throws Exception{

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
            LogUtility.logUtility().log2out("fail, Email has been sign up ");
            JSONObject jsonObject = JSONObject.fromObject("{action:'sign_up',StatusCode:5,message:'This Email has been sign up'}");
            connection.send(jsonObject.toString());

        } else {

            prep = connectionToSql.prepareStatement(
                    "select count(*) as rowCount from Users where nickname = ?;");
            prep.setString(1, userName);

            resultSet = prep.executeQuery();

            if (resultSet.getInt("rowCount") != 0) { //username has been sign up
                LogUtility.logUtility().log2out("fail, user name has been sign up");
                JSONObject jsonObject = JSONObject.fromObject("{action:'sign_up',StatusCode:6,message:'This UserName has been sign up'}");
                connection.send(jsonObject.toString());
            } else {
                LogUtility.logUtility().log2out("sign up is ok");
                DEPFS.createDirectory(DistributeServer.absolutePathOfRuntimeUsers + "/" + userName);
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
     * Action when user send message to get model description
     *
     * @param connection
     *
     */
    public static void getModel(WebSocket connection) throws SQLException {
        String userID = (String)DistributeServerHearken.currentUsers.get(connection);

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep = connectionToSql.prepareStatement(  //email has been sign up
                "select description,id from Models where owner = ?;");
        prep.setString(1, userID);
        ResultSet resultSet = prep.executeQuery();
        String modelDescription = "";
        String modelId = "";
        while (resultSet.next()) {
            modelDescription += XMLGenerator.ComponentXML(resultSet.getString("description"), resultSet.getString("id"));
        }
        modelDescription = XMLGenerator.ComponentXML(modelDescription);
        JSONObject jsonObject = JSONObject.fromObject("{action:'get model',StatusCode:0,message:\""+modelDescription+"\"}");
        connection.send(jsonObject.toString());

        LogUtility.logUtility().log2out(jsonObject.toString());

    }

    /**
     * Action when user send message to link the models
     *
     * @param connection, data
     *                    data is composed of model description xml
     *
     */
    public static void linkModel(WebSocket connection, String data) throws DocumentException, SQLException {
        Map idToName = new ConcurrentHashMap<String, String>();

        Document document = DocumentHelper.parseText(data);
        Element root = document.getRootElement();
        List parts = root.element("parts").elements("part");

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();
        PreparedStatement prep = connectionToSql.prepareStatement(
                "select owner from Models where id= ?;");

        for (Iterator it = parts.iterator(); it.hasNext();) {
            Element component = (Element) it.next();
            String partId = component.attributeValue("componentId");
            prep.setString(1, partId);
            ResultSet resultSet = prep.executeQuery();
            if(resultSet.next()){
                String owner = resultSet.getString("owner");
                PreparedStatement preparedStatement =connectionToSql.prepareStatement(
                        "select nickname from Users where sid= ?;"
                );
                preparedStatement.setString(1, owner);
                ResultSet resultSet1 = preparedStatement.executeQuery();
                if(resultSet1.next()) {
                    String userName = resultSet1.getString("nickname");
                    idToName.put(partId, userName);
                    LogUtility.logUtility().log2out("partId : userName = " + partId + ":" + userName);
                }
                resultSet1.close();
                preparedStatement.close();
            }
        }
        prep.close();
        connectionToSql.close();
    }

    /**
     * Action when user send message to compile the models
     *
     * @param connection, data
     *                    data is composed of data file and model data flow XML
     *
     */
    public static void compileModel(WebSocket connection, String data) throws DocumentException {
        SAXReader reader =new SAXReader();
        Document document = DocumentHelper.parseText(data);
        Element root = document.getRootElement();
        List components = root.elements("part");
        for (Iterator it = components.iterator(); it.hasNext();) {
            Element component = (Element) it.next();
            String partId = component.attributeValue("id");
            //do something
        }
        String docXmlText=document.asXML();
        String rootXmlText=root.asXML();
        Element memberElm=root.element("member");
        String memberXmlText=memberElm.asXML();
    }
    /**
     * @TODO
     */

    public static void runModel() {

    }
}
