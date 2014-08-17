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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.prettyx.Common.*;
import org.prettyx.DistributeServer.Modeling.SimFile;
import org.prettyx.DistributeServer.Network.DistributeServerHearken;
import org.prettyx.DistributeServer.Settings.SettingsCenter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        DistributeServer distributeServer = new DistributeServer();
        distributeServer.initialize();

        DistributeServerHearken distributeServerHearken = new DistributeServerHearken(Integer.valueOf(distributeServer.settingsCenter.getSetting("Network", "Port")));
        distributeServerHearken.checkAndStart();

        distributeServer.test5();
//        distributeServer.test();
    }


    public void test(){
        String string = "   dsjf   ";
        System.out.println(DEPFS.removeSpace(string));
    }
    public void test5() throws DocumentException, SQLException {
        Map idToName = new ConcurrentHashMap<String, String>();
        List<String> sourceToTarget = new ArrayList<String>();
        String data = "<component><from/><to/><parts>" +
                "<part id='87fd1561-aed8-44ab-9389-32651b29a295' " +
                "componentId='55e12a6e-fedf-43c5-ab0d-4a395fb499ff'>" +
                "<inputId portName='in1' sourcePortName=''/><inputId portName='in2' sourcePortName=''/>" +
                "<outputId portName='out' targetPortName='message'>e87c5c1e-3cbf-475a-a7ea-90f4c7ddc392</outputId>" +
                "</part><part id='e87c5c1e-3cbf-475a-a7ea-90f4c7ddc392' " +
                "componentId='a470904c-5dc8-418e-9288-1f854b9bb50d'><inputId portName='message' " +
                "sourcePortName='out'>87fd1561-aed8-44ab-9389-32651b29a295</inputId><outputId portName='out'" +
                " targetPortName=''/></part></parts></component>\n";
        Document document = DocumentHelper.parseText(data);
        Element root = document.getRootElement();
        List parts = root.element("parts").elements("part");

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();
        PreparedStatement prep = connectionToSql.prepareStatement(  //email has been sign up
                "select modelname from Models where id= ?;");

        for (Iterator it = parts.iterator(); it.hasNext();) {
            Element component = (Element) it.next();
            String partId = component.attributeValue("componentId");
            prep.setString(1, partId);
            ResultSet resultSet = prep.executeQuery();
            if(resultSet.next()){
                String partName = resultSet.getString("modelname");
                idToName.put(partId, partName);
                System.out.println("partname : id = " + partName + ":" + partId);
            }
        }
        prep.close();
        connectionToSql.close();
//        for (Iterator it = parts.iterator(); it.hasNext();) {
//            Element component = (Element) it.next();
//            String partId = component.attributeValue("id");
//            String partName = (String)idToName.get(partId);
//            List outPorts = component.elements("outputId");
//            for( Iterator ot = outPorts.iterator(); ot.hasNext();){
//                Element outPort = (Element)ot.next();
//                String portName = outPort.attributeValue("portName");
//                String targetPortName = outPort.attributeValue("targetPortName");
//                String targetModelId = outPort.getTextTrim();
//                String targetModelName = (String) idToName.get(targetModelId);
//                String source = partName + "." + portName;
//                String target = targetModelName + "." + targetPortName;
//                sourceToTarget.add(source+"+"+target);
//            }
//
//        }
//        System.out.println(sourceToTarget);

        //这里还要加入获取component 的 ID  然后得到sim文件的路径
//        try {
//            File file = new File("/Users/XieFan/Desktop/oms3.prj.examples-basic/simulation/ex06_TimeIteration.sim");
//            FileInputStream inputStream = new FileInputStream(file);
//            String content = DEPFS.readFile(inputStream).replace("\'", "\"");
//            SimFile simFile = new SimFile(content);
////            System.out.println(simFile.toString());
//
//        }catch (Exception e){
//
//        }


    }


}
