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
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.prettyx.Common.*;
import org.prettyx.DistributeServer.DistributeServer;
import org.prettyx.DistributeServer.Modeling.Model;
import org.prettyx.DistributeServer.Modeling.Sim;
import org.prettyx.DistributeServer.Modeling.SimFile;

import java.io.*;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipOutputStream;

/**
 * Action Handler
 * handle the message having different action
 * action is dived into login, logout, sign up, get model, run model
 *
 */

public class ActionHandler {

    /**
     * Action when user using userName or email to login
     *
     * @param connection, data
     *
     */
    public static void userNameOrEmailToLogin(WebSocket connection, String data) throws Exception {

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
    public static void userSidTologin(WebSocket connection, String sid){
        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();
        try {
            PreparedStatement prep = connectionToSql.prepareStatement(
                    "select count(*) as rowCount from Users where token ='" + sid + "';");
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
        }catch (Exception e){
//            e.printStackTrace();
        }

    }

    /**
     * Action when user to log out
     *
     * @param connection, sid
     *
     */

    public static void logOut(WebSocket connection) throws SQLException {
        String userID = (String)DistributeServerHearken.currentUsers.get(connection);

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep = connectionToSql.prepareStatement(
                "select count(*) as rowCount from Users where sid ='"+ userID +"';");

        ResultSet resultSet = prep.executeQuery();
        if(resultSet.getInt("rowCount") != 0) {
            LogUtility.logUtility().log2out("log out is ok");
            JSONObject jsonObject = JSONObject.fromObject("{action:'logOut',StatusCode:1,message:'ok'}");
            connection.send(jsonObject.toString());
            DistributeServerHearken.currentUsers.remove(connection, userID);
        }
        else {
            LogUtility.logUtility().log2out("log out failed");
            JSONObject jsonObject = JSONObject.fromObject("{action:'logOut',StatusCode:0,message:'failed'}");
            connection.send(jsonObject.toString());
        }
        resultSet.close();
        prep.close();
        connectionToSql.close();


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
    public static void getModel(WebSocket connection) throws SQLException, ClassNotFoundException {
        String userID = (String)DistributeServerHearken.currentUsers.get(connection);
        String modelDescription = XMLGenerator.ComponentsXML(userID);

        JSONObject jsonObject = JSONObject.fromObject("{action:'get model',StatusCode:0,message:\""+modelDescription+"\"}");
        connection.send(jsonObject.toString());

        LogUtility.logUtility().log2out(jsonObject.toString());

    }

    /**
     * Action when user send message to generate the sim file
     *
     * @param connection, data
     *                    data is composed of model description xml
     *
     */
    public static void compileModel(WebSocket connection, String data) throws DocumentException, SQLException, IOException {

        SimFile simFile = new SimFile();
        Sim sim = new Sim();
        Model model = new Model();
        Map newComponentName = new HashMap<String, String>(); // package.class -> new name
        Set components = new HashSet<String>();
        Map parameter = new HashMap<String,String>();   //class.var -> value

        Map idToOwnerName = new ConcurrentHashMap<String, String>(); //model id -> owner name
        Map idToModelName = new ConcurrentHashMap<String, String>(); //model id -> model name
        Map partIdToModelId = new ConcurrentHashMap<String, String>(); // part id -> model id
        List<String> sourceToTarget = new ArrayList<String>(); //source port name + target port name
        List<SimFile> simFiles = new ArrayList<SimFile>(); //collect all the sim files which the new component use

        Document document = DocumentHelper.parseText(data);
        Element root = document.getRootElement();
        String componentName = root.element("name").getText();
        String currentUserName = "";
        LogUtility.logUtility().log2out("component name = " + componentName);
        List parts = root.element("parts").elements("part");

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep =connectionToSql.prepareStatement(
                "select nickname from Users where sid= ?;"
        );
        prep.setString(1, (String) DistributeServerHearken.currentUsers.get(connection));
        ResultSet resultSet = prep.executeQuery();
        if(resultSet.next()) {
            currentUserName = resultSet.getString("nickname");
            LogUtility.logUtility().log2out("current user name:" + currentUserName);
        }
        resultSet.close();
        prep.close();

        prep = connectionToSql.prepareStatement(
                "select owner,modelname from Models where id= ?;");

        for (Iterator it = parts.iterator(); it.hasNext();) {
            Element component = (Element) it.next();
            String modeId = component.attributeValue("componentId");
            String partId = component.attributeValue("id");
            partIdToModelId.put(partId, modeId);

            prep.setString(1, modeId);
            resultSet = prep.executeQuery();
            if(resultSet.next()){
                String owner = resultSet.getString("owner");
                String modelName = resultSet.getString("modelname");
                idToModelName.put(modeId, modelName);
                PreparedStatement preparedStatement =connectionToSql.prepareStatement(
                        "select nickname from Users where sid= ?;"
                );
                preparedStatement.setString(1, owner);
                ResultSet resultSet1 = preparedStatement.executeQuery();
                if(resultSet1.next()) {
                    String userName = resultSet1.getString("nickname");
                    idToOwnerName.put(modeId, userName);
                    LogUtility.logUtility().log2out("partId : userName = " + modeId + ":" + userName);
                }
                resultSet1.close();
                preparedStatement.close();
            }
        }
        prep.close();
        connectionToSql.close();

        if(!idToOwnerName.isEmpty()) {
            //copy files
            String newModelPath = DistributeServer.absolutePathOfRuntimeUsers + "/"
                    + currentUserName + "/" + componentName;
            DEPFS.createDirectory(newModelPath);
            DEPFS.createDirectory(newModelPath + "/" + "dist");
            DEPFS.createDirectory(newModelPath + "/" + "input");
            DEPFS.createDirectory(newModelPath + "/" + "output");

            Iterator ita = null;
            ita = idToOwnerName.entrySet().iterator();
            while (ita.hasNext()) {
                Map.Entry entry = (Map.Entry) ita.next();
                String modelname = (String) idToModelName.get((String) entry.getKey());
                String owername = (String) entry.getValue();
                String sourcePath = DistributeServer.absolutePathOfRuntimeUsers + "/"
                        + owername + "/" + modelname + "/" + "dist/";
                String targetPath = newModelPath + "/" + "dist/";
                DEPFS.copyDirectory(sourcePath, targetPath);
                File oldSimFile = new File(DistributeServer.absolutePathOfRuntimeUsers + "/"
                        + owername + "/" + modelname + "/" + "simulation.sim");
                if (oldSimFile.exists()) {
                    simFiles.add(new SimFile(DEPFS.readFile(oldSimFile)));
                }
            }

            //create sim file

            //only single model to run
            if (parts.size() == 1 && simFiles.size() == 1) {
                simFile = simFiles.get(0);
                for (Iterator it = parts.iterator(); it.hasNext(); ) {
                    Element component = (Element) it.next();
                    String partName = (String) idToModelName.get(partIdToModelId.get(component.attributeValue("id")));

                    //get parameter values
                    List inPorts = component.elements("input");
                    for (Iterator ot = inPorts.iterator(); ot.hasNext(); ) {
                        Element inPort = (Element) ot.next();
                        String portName = inPort.attributeValue("portName");
                        String portValue = inPort.attributeValue("value");
                        String inputFileName = inPort.attributeValue("fileName");

//                        components.add(partName + "." + portName.split("\\.")[0]);
                        if (portValue != "") {

                            if (inputFileName != "") {
                                File inputFile = new File(newModelPath + "/" + "input" + "/" + inputFileName);
                                DEPFS.writeFile(inputFile, portValue.replace("<br>","\n"));
                                String source = partName + "." + portName;
                                parameter.put(source, "\"$oms_prj/input/" + inputFileName + "\"");

                            } else {
                                String source = partName + "." + portName;
                                parameter.put(source, portValue);
                            }
                            String originalComponentName = portName.substring(0, portName.lastIndexOf("."));
                            String originalComponentKey = simFile.getSim().getModel().getComponentKey(originalComponentName);
                            if (originalComponentKey != null) {
                                String originalComponentName1 = originalComponentKey + "."
                                        + portName.substring(portName.lastIndexOf(".") + 1, portName.length());
                            simFile.getSim().getModel().setParameter(originalComponentName1, (String) parameter.get(partName + "." + portName));
                                System.out.println(originalComponentName1);
                            }

                        }
                    }
                }
                //using simFile to generate sim file

                File simulation = new File(DistributeServer.absolutePathOfRuntimeUsers + "/"
                        + currentUserName + "/" + componentName + "/simulation.sim");
                DEPFS.writeFile(simulation, simFile.toString());
                runModel(connection,data);

            }
            else {
                // get connected port
                for (Iterator it = parts.iterator(); it.hasNext(); ) {
                    Element component = (Element) it.next();
                    String partName = (String) idToModelName.get(partIdToModelId.get(component.attributeValue("id")));
                    List outPorts = component.elements("output");
                    for (Iterator ot = outPorts.iterator(); ot.hasNext(); ) {
                        Element outPort = (Element) ot.next();
                        String portName = outPort.attributeValue("portName");
                        String targetPortName = outPort.attributeValue("targetPortName");
                        String targetModelId = outPort.attributeValue("targetPortId");
                        String targetModelName = (String) idToModelName.get(partIdToModelId.get(targetModelId));
                        String source = partName + "." + portName;
                        String target = targetModelName + "." + targetPortName;
                        components.add(partName + "." + portName.split("\\.")[0]);
                        components.add(targetModelName + "." + targetPortName.split("\\.")[0]);
                        sourceToTarget.add(source + "+" + target);

                    }

                    //get parameter values
                    List inPorts = component.elements("input");
                    for (Iterator ot = inPorts.iterator(); ot.hasNext(); ) {
                        Element inPort = (Element) ot.next();
                        String portName = inPort.attributeValue("portName");
                        String portValue = inPort.attributeValue("value");
                        String inputFileName = inPort.attributeValue("fileName");
                        components.add(partName + "." + portName.split("\\.")[0]);

                        if (inputFileName != "") {
                            File inputFile = new File(newModelPath + "/" + "input" + "/" + inputFileName);
                            DEPFS.writeFile(inputFile, portValue);
                            String source = partName + "." + portName;
                            parameter.put(source, "$oms_prj/input/" + portValue);

                        } else if (portValue != "") {
                            String source = partName + "." + portName;
                            parameter.put(source, portValue);
                        }

                    }
                }

                //use components to component element
                ita = components.iterator();
                for (int i = 1; ita.hasNext(); i++) {
                    String value = (String) ita.next();
                    String newName = "c" + i;
                    newComponentName.put(value, newName);
                    model.setComponent(newName, value);

                }

                //use sourceToTarget to connect component
                for (int i = 0; i < sourceToTarget.size(); i++) {
                    String[] componentInfo = sourceToTarget.get(i).split("\\+");
                    String preSourceName = componentInfo[0].split("\\.")[0] + "." + componentInfo[0].split("\\.")[1];
                    String laterTargetName = componentInfo[1].split("\\.")[0] + "." + componentInfo[1].split("\\.")[1];
                    model.setConnect(componentInfo[0].replace(preSourceName, (String) newComponentName.get(preSourceName)),
                            componentInfo[1].replace(laterTargetName, (String) newComponentName.get(laterTargetName)));

                }

                //user parameter to set parameter value
                if (!parameter.isEmpty()) {
                    ita = parameter.entrySet().iterator();
                    while (ita.hasNext()) {
                        Map.Entry entry = (Map.Entry) ita.next();
                        String key = (String) entry.getKey();
                        String[] pots = key.split("\\.");
                        String preName = key.replace(pots[0] + "." + pots[1], (String) newComponentName.get(pots[0] + "." + pots[1]));
                        String value = "\"" + (String) entry.getValue() + "\"";
                        model.setParameter(preName, value);
                    }
                }


                sim.setModel(model);
                simFile.setSim(sim);
                simFile.setImport("import static oms3.SimBuilder.instance as OMS3");

                //using simFile to generate sim file

                File simulation = new File(DistributeServer.absolutePathOfRuntimeUsers + "/"
                        + currentUserName + "/" + componentName + "/simulation.sim");
                DEPFS.writeFile(simulation, simFile.toString());
                runModel(connection,data);

//                JSONObject jsonObject = JSONObject.fromObject("{action:'compile',StatusCode:1,message:'success'}");
//                connection.send(jsonObject.toString());
            }
        }
    }

    /**
     * @TODO
     */

    public static void runModel( final WebSocket connection, String data) throws SQLException, DocumentException {


        Document document = DocumentHelper.parseText(data);
        Element root = document.getRootElement();
        String componentName = root.element("name").getText();

        DBOP dbop = new DBOP();
        String currentUserName = "";
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep =connectionToSql.prepareStatement(
                "select nickname from Users where sid= ?;"
        );
        prep.setString(1, (String) DistributeServerHearken.currentUsers.get(connection));
        ResultSet resultSet = prep.executeQuery();
        if(resultSet.next()) {
            currentUserName = resultSet.getString("nickname");
            LogUtility.logUtility().log2out("current user name:" + currentUserName);
        }
        resultSet.close();
        prep.close();

        String path = DistributeServer.absolutePathOfRuntimeUsers +"/"+ currentUserName +"/"+ componentName;

        final String zipFileName = "/tmp/"+ UUID.randomUUID() +".zip";
        try {
            File file = new File(path);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            DEPFS.zip(file.getPath(), file.getPath().length(), out);
            out.closeEntry();
            out.close();

            try {
                WebSocketClient webSocketClient = new WebSocketClient(
                        new URI("ws://"+ DistributeServer.settingsCenter.getSetting("Network", "ComputeServerAddress"))
                ) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        try {
                            send(JSONObject.fromObject(
                                    "{action:"+ Message.D_C_RUN +", sid:'', data:\""+ DEPFS.encodeBase64File(zipFileName).replace("\n","") +"\"}"
                            ).toString());
                            DEPFS.removeFile(zipFileName);
                        } catch (Exception e){
                            LogUtility.logUtility().log2err(e.getMessage());
                        }
                    }

                    @Override
                    public void onMessage(String s) {
                        // TODO Process Returned Data
                        JSONObject jsonObject = JSONObject.fromObject("{action:'run',StatusCode:1,message:'"+s+"'}");
                        connection.send(jsonObject.toString());
                        System.out.println(jsonObject.toString());

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

        } catch (Exception e) {
            LogUtility.logUtility().log2err(e.getMessage());
        }
    }


    public static void runModelTest() {

        String path = "/Users/PJW/Desktop/ABC";
        final String zipFileName = "/tmp/"+ UUID.randomUUID() +".zip";
        try {
            File file = new File(path);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            DEPFS.zip(file.getPath(), file.getPath().length(), out);
            out.closeEntry();
            out.close();

            try {
                WebSocketClient webSocketClient = new WebSocketClient(
                        new URI("ws://"+ DistributeServer.settingsCenter.getSetting("Network", "ComputeServerAddress"))
                ) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        try {
                            send(JSONObject.fromObject(
                                    "{action:"+ Message.D_C_RUN +", sid:'', data:\""+ DEPFS.encodeBase64File(zipFileName).replace("\n","") +"\"}"
                            ).toString());
                            DEPFS.removeFile(zipFileName);
                        } catch (Exception e){
                            LogUtility.logUtility().log2err(e.getMessage());
                        }
                    }

                    @Override
                    public void onMessage(String s) {
                        System.out.println(s);
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

        } catch (Exception e) {
            LogUtility.logUtility().log2err(e.getMessage());
        }
    }
}
