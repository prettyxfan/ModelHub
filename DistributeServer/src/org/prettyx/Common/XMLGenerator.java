// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.Common;

import oms3.util.Annotations;
import org.prettyx.DistributeServer.DistributeServer;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class XMLGenerator {

    public static String ComponentsXML(String userId) throws SQLException, ClassNotFoundException {
        String xml = "";
        String component = "";
        Map<String, String> input = new HashMap<String, String>();
        Map<String, String> output = new HashMap<String, String>();

        DBOP dbop = new DBOP();
        Connection connectionToSql = dbop.getConnection();

        PreparedStatement prep = connectionToSql.prepareStatement(
                "select modelname,description,id from Models where owner = ?;");
        prep.setString(1, userId);
        ResultSet resultSet = prep.executeQuery();

        while (resultSet.next()) {
            String modelName = "";
            String modelDescription = "";
            String modelId = "";
            String userName = "";
            String rootPath = "";
            input.clear();
            output.clear();

            modelName = resultSet.getString("modelname");
            modelDescription = resultSet.getString("description");
            modelId = resultSet.getString("id");
            PreparedStatement preparedStatement = connectionToSql.prepareStatement(
                    "select nickname from Users where sid = ?;");
            preparedStatement.setString(1,userId);
            ResultSet resultSet1 = preparedStatement.executeQuery();
            if(resultSet1.next()) {
                userName = resultSet1.getString("nickname");
            }
            if(userName != ""){
                rootPath = DistributeServer.absolutePathOfRuntimeUsers
                        + "/" + userName + "/" + modelName + "/build/";
                ClassLoaderUtils classLoaderUtils = new ClassLoaderUtils(rootPath);
                List<Class<?>> s = classLoaderUtils.getServiceClassList();
                for(int i=0; i<s.size(); i++){
                    Class<?> cla = s.get(i);
                    try {
                        Field[] fields = cla.getDeclaredFields();
                        for (int j = 0; j < fields.length; j++) {
                            Field field = fields[j];
                            String fieldType = field.getType().toString();
                            String []typeSplite = fieldType.split("\\.");
                            fieldType = typeSplite[typeSplite.length-1];
                            String fieldName = cla.getSimpleName() + "." + field.getName();
                            if(Annotations.isIn(field))
                                input.put(fieldName,fieldType);
                            else if(Annotations.isOut(field))
                                output.put(fieldName,fieldType);
                        }
                    }catch (NoClassDefFoundError e){
                        continue;
                    }

                }
                component += ComponentXML(modelId,modelName,modelDescription,input,output);
            }
            resultSet1.close();
            preparedStatement.close();

        }
        resultSet.close();
        prep.close();

        xml = "<components>" + component + "</components>";

        return xml;
    }
    public static String ComponentXML(String modelId, String modelName, String modelDescription, Map input, Map output){

        String component = "";
        component = "<component>" +
                "<componentId>" + modelId + "</componentId>" +
                "<componentName>" + modelName + "</componentName>" +
                "<componentDescription>" + modelDescription + "</componentDescription>" +
                "<inputs>" ;
        Iterator ita = null;
        ita = input.entrySet().iterator();
        while (ita.hasNext()) {
            Map.Entry entry = (Map.Entry) ita.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            component += "<input><inputName>"+key+"</inputName><inputType>"+value+"</inputType></input>";
        }
        component += "</inputs><outputs>";

        ita = output.entrySet().iterator();
        while (ita.hasNext()) {
            Map.Entry entry = (Map.Entry) ita.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            component += "<output><outputName>"+key+"</outputName><outputType>"+value+"</outputType></output>";
        }
        component += "</outputs>";
        component += "<parameters><parameter><parameterName></parameterName>" +
                "<parameterType></parameterType></parameter></parameters>";
        component += "</component>";

        return component;
    }

    private String location;

    private String content;

    public XMLGenerator(String url) {

        location = url;
        File file = new File(url);
        try {
            content = DEPFS.readFile(file);
        }catch (Exception e) {

        }
    }

    public void parseContent(){

        String componentLocation = location;
        String componentName = "";
        String componentDescription = "";
        String inputs = "";
        String outputs ="";
        String parameters = "";

        String []lineContent = content.split("\n");

        for(int i=0; i<lineContent.length; i++) {

            if( lineContent[i].contains("/*")){
                while(!lineContent[i].contains("*/")){
                    componentDescription += lineContent[i];
                    i++;
                }
            }
        }




    }

    public String createXML(){

       String componentXML = "";
        return  componentXML;
    }

    public void showContent(){

        System.out.println(content);
    }
//    public static void main(String[] args) {
//
//        String url = "/Users/XieFan/Desktop/OMS-3.2-Examples-Basic/simulation/ex00_HelloWorld.sim";
//        XMLCreater reflector = new XMLCreater(url);
//        reflector.showContent();
//
//    }




}