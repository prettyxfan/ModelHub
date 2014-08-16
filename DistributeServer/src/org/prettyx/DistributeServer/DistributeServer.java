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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.prettyx.Common.*;
import org.prettyx.DistributeServer.Modeling.Sim;
import org.prettyx.DistributeServer.Modeling.SimFile;
import org.prettyx.DistributeServer.Network.DistributeServerHearken;
import org.prettyx.DistributeServer.Settings.SettingsCenter;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DistributeServer {

    private SettingsCenter settingsCenter;
    public static String absolutePathOfDB = null;

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

        return StatusCodes.SUCCESS;
    }

    /**
     * The Main Application
     * @param args
     *          Command Line Parameters
     */
    public static void main(String[] args) throws Exception {
        DistributeServer distributeServer = new DistributeServer();
        distributeServer.initialize();

        DistributeServerHearken distributeServerHearken = new DistributeServerHearken(Integer.valueOf(distributeServer.settingsCenter.getSetting("Network", "Port")));
        distributeServerHearken.checkAndStart();

//        distributeServer.test4();

//        Map s = XMLParser.parserXmlFromString("<components><component>1</component><component>2</component><component>3</component></components>");
//        System.out.println(s.toString());
        distributeServer.test8();

    }
    public void test6() throws IOException {
        File file = new File("/Users/XieFan/Desktop/oms3.prj.examples-basic/simulation/ex00_HelloWorld.sim");
        FileInputStream inputStream = new FileInputStream(file);
        String [] content = DEPF.readFile(inputStream).split("\n");
        SimFile simFile = new SimFile();
        for(int i= 0; i<content.length; i++){
            String line = content[i];
            if(line.contains("import")){
                simFile.setImport(line);
                System.out.println(simFile.getImport());
                continue;
            }
            if(line.contains("sim")){
                String simContent = "";
                while (i<content.length){
                    simContent += content[i] + "\n";
                    i++;
                }
//                simFile.setSim(simContent);
                System.out.println(simContent);
                break;
            }

        }
        inputStream.close();
    }
    public void test7(String string){
        String [] content = string.split("\n");
        for(int i = 0; i<content.length; i++){
            String line = content[i];
            if(i == 0){
                Pattern pattern=Pattern.compile("\\(.*?\\)");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    System.out.println(matcher.group());
                    String name = matcher.group();
                    if(matcher.group() != "") {
                        System.out.print("start:" + matcher.start());
                        int start = matcher.start();
                        System.out.println(" end:" + matcher.end());
                        int end = matcher.end();
                        name = line.substring(start + 1, end - 1);
                        System.out.println(name);
                        pattern = Pattern.compile(":");
                        String[] name1 = pattern.split(name);
                        if (name1[0].trim().equals("name")) {
                            System.out.println(name1[1]);
                        }
                    }
                }
                continue;
            }
            else if(line.contains("resource")){
                Pattern pattern=Pattern.compile("\"");
                String []r = pattern.split(line);
                if(r[0].trim().equals("resource")){
                    System.out.println(r[1]);
                }
            }
        }

    }
    public void test8(){



    }
    public void test(){

        String absolutePathOfDB = this.settingsCenter.getSetting("Init", "RuntimeDatabasePath");

        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + absolutePathOfDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(5);

            ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table'\n ORDER BY name;");

            LogUtility.logUtility().log2out("First Table: " + resultSet.getString(1));

        } catch (Exception e){

            LogUtility.logUtility().log2err(e.getMessage());
        }
    }

    public void test2(){
        DBOP dbop = new DBOP();
        dbop.Operation("SELECT name FROM sqlite_master WHERE type='table'\n ORDER BY name;");
    }

    public void test3(){
        LogUtility.logUtility().log2out(UUID.randomUUID().toString());
    }
    public void test4() {
        String json="{name:'Java',price:52.3}";
        JSONObject object=JSONObject.fromObject(json);
        System.out.println(object.get("name")+" "+object.get("price"));


    }
    public void test5() throws DocumentException, SQLException {
        Map idToName = new ConcurrentHashMap<String, String>();
        List<String> sourceToTarget = new ArrayList<String>();
        String data = "<component><from/><to/><parts><part id=\"55e12a6e-fedf-43c5-ab0d-4a395fb499ff\">" +
                "<inputId portName=\"in1\" sourcePortName=\"out\">5fb439fe-bfb6-45a8-b495-eb19a796941d</inputId>" +
                "<inputId portName=\"in2\" sourcePortName=\"\"/><outputId portName=\"out\" targetPortName=\"\"/>" +
                "</part><part id=\"a470904c-5dc8-418e-9288-1f854b9bb50d\"><inputId portName=\"message\" sourcePortName=\"\"/><outputId portName=\"out\" targetPortName=\"in1\">8fe4dbc7-fbd9-46e0-9e39-3ad9b21053df</outputId></part></parts></component>";
        Document document = DocumentHelper.parseText(data);
        Element root = document.getRootElement();
        List parts = root.element("parts").elements("part");

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();
        PreparedStatement prep = connectionToSql.prepareStatement(  //email has been sign up
                "select modelname from Models where id= ?;");

        for (Iterator it = parts.iterator(); it.hasNext();) {
            Element component = (Element) it.next();
            String partId = component.attributeValue("id");
            prep.setString(1, partId);
            ResultSet resultSet = prep.executeQuery();
            if(resultSet.next()){
                String partName = resultSet.getString("modelname");
                idToName.put(partId, partName);
                System.out.println("partname = " + partName);
            }
        }
        prep.close();
        connectionToSql.close();
        for (Iterator it = parts.iterator(); it.hasNext();) {
            Element component = (Element) it.next();
            String partId = component.attributeValue("id");
            String partName = (String)idToName.get(partId);
            List outPorts = component.elements("outputId");
            for( Iterator ot = outPorts.iterator(); ot.hasNext();){
                Element outPort = (Element)ot.next();
                String portName = outPort.attributeValue("portName");
                String targetPortName = outPort.attributeValue("targetPortName");
                String targetModelId = outPort.getTextTrim();
                String targetModelName = (String) idToName.get(targetModelId);
                String source = partName + "." + portName;
                String target = targetModelName + "." + targetPortName;
                sourceToTarget.add(source+"+"+target);
            }

        }
        System.out.println(sourceToTarget);


    }


}
