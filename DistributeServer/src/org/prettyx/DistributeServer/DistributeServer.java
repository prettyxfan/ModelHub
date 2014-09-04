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

import com.sun.tools.javac.file.JavacFileManager;
import net.sf.json.JSONObject;
import oms3.annotations.In;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ibex.nestedvm.util.Seekable;
import org.prettyx.Common.*;
import org.prettyx.DistributeServer.Modeling.Model;
import org.prettyx.DistributeServer.Modeling.Sim;
import org.prettyx.DistributeServer.Modeling.SimFile;
import org.prettyx.DistributeServer.Network.DistributeServerHearken;
import org.prettyx.DistributeServer.Settings.SettingsCenter;
import sun.misc.ClassLoaderUtil;
import sun.rmi.rmic.iiop.ClassPathLoader;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.server.RMIClassLoader;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import oms3.util.Annotations;
import oms3.util.Components;
import javax.tools.*;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        distributeServer.test0();

    }

    public static void test0() {
        SimFile simFile = new SimFile();
        Sim sim = new Sim();
        Model model = new Model();
        System.out.println(simFile.toString());
    }

    public void test1() throws Exception {
        String userID = "51c73107-1e8c-4e90-a730-4b2a688273df";
        String modelDescription = XMLGenerator.ComponentsXML(userID);

        JSONObject jsonObject = JSONObject.fromObject("{action:'get model',StatusCode:0,message:\""+modelDescription+"\"}");

        LogUtility.logUtility().log2out(jsonObject.toString());
    }

    public void test2() throws Exception {

        String filePath = "/Users/XieFan/Documents/ModelHub/Runtime/Users/PengJingwen";
        ClassLoaderUtils classLoaderUtils = new ClassLoaderUtils(filePath);
        List<Class<?>> s = classLoaderUtils.getServiceClassList();
        List<Class<?>> undo = new LinkedList<Class<?>>();
        for(int i=0; i<s.size(); i++){

            Class<?> cla = s.get(i);
            try {
                Field[] fields = cla.getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    Class fieldType = field.getType();

                    System.out.println(cla.getName() + " " +field.getName() + " " + fieldType);
                    System.out.println(Annotations.isIn(field));

                }
            }catch (NoClassDefFoundError e){
                undo.add(cla);
                continue;
            }

        }
             for(int i=0; i<undo.size(); i++){

                 Class<?> cla = undo.get(i);
                 System.out.println(cla.getName());

             }

    }
    public void test5() throws DocumentException, SQLException {
        Map idToName = new ConcurrentHashMap<String, String>();
        List<String> sourceToTarget = new ArrayList<String>();
        String data = "<component><from/><to/><name>Test</name><parts>" +
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
                    System.out.println("partId : userName = " + partId + ":" + userName);
                }
                resultSet1.close();
                preparedStatement.close();
            }
        }
        prep.close();
        connectionToSql.close();
        for (Iterator it = parts.iterator(); it.hasNext();) {
            Element component = (Element) it.next();
            String partId = component.attributeValue("id");
            String partName = (String)idToName.get(partId);
            LogUtility.logUtility().log2out("part name:" + partName);
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
                System.out.println("source:" + source + " target:" + target);
            }

        }
        System.out.println(sourceToTarget);

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
