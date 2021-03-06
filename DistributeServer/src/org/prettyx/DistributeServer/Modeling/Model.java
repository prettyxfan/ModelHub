// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------

package org.prettyx.DistributeServer.Modeling;

import org.apache.commons.collections.map.ListOrderedMap;
import org.prettyx.Common.DEPFS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model is represent the element "model" of the sim file
 * including other subelement
 */

public class Model {
    private String modelParameterName = "";
    private String modelParameterValue = "";
    private String iter = "";
    private String Sim_while = "";
    private String Sim_until = "";
    private String Sim_if = "";
    private Map component = new ListOrderedMap();
    private Connect connect = new Connect();
    private Map feedback = new ListOrderedMap();
    private Map logging = new ListOrderedMap();
    private Parameter parameter = new Parameter();

    public String getComponentKey(String value) {
        Iterator ita = null;
        ita = component.entrySet().iterator();
        while (ita.hasNext()) {
            Map.Entry entry = (Map.Entry) ita.next();
            String key = (String) entry.getKey();
            String data = (String) entry.getValue();
            if(data.equals(value)){
                return key;
            }
        }
        return null;
    }

    /**
     * default constructor with none input parameter
     */
    public Model(){
        parameter = new Parameter();
    }

    /**
     * Using the content of the model element to create a model object
     * @param string
     */
    public Model(String string){

        String [] content = string.split("\n");
        for(int i = 0; i<content.length; i++) {

            String line = content[i];

            if(i == 0){
                Pattern pattern=Pattern.compile("\\(.*?\\)");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    String name = "";
                    if(matcher.group() != "") {
                        int start = matcher.start();
                        int end = matcher.end();
                        name = line.substring(start + 1, end - 1);
                        pattern = Pattern.compile(":");
                        String[] name1 = pattern.split(name);
                        if (name1.length != 1) {
                            setModelParameter(DEPFS.removeSpace(name1[0]),DEPFS.removeSpace(name1[1].replace("\"", "")));
                        }
                    }
                }
                continue;
            }
            else if(line.contains("components")){

                while (i<content.length){
//                    System.out.println(line);
                    if(line.contains("}")) break;
                    line = line.split("//")[0];
                    if(line.trim() != ""){
                        Pattern pattern=Pattern.compile("\"");
                        String []r = pattern.split(line);

                        if(r.length >= 4){
                            String object = DEPFS.removeSpace(r[1]);
                            String name = DEPFS.removeSpace(r[3]);
                            setComponent(object, name);
                        }
                        else if(r.length >= 2){
                            String object = DEPFS.removeSpace(r[0]);
                            String name = DEPFS.removeSpace(r[1]);
                            setComponent(object, name);
                        }
                    }
                    i++;
                    line = content[i];

                }
                continue;

            }
            else if(line.contains("connect")){
                while (i<content.length){
//                    System.out.println(line);
                    if(line.contains("}")) break;
                    line = line.split("//")[0];
                    if(line.trim() != ""){
                        Pattern pattern=Pattern.compile("\"");
                        String []r = pattern.split(line);

                        if(r.length >= 4){
                            String outputVariable = DEPFS.removeSpace(r[1]);
                            String inputVariable = DEPFS.removeSpace(r[3]);
                            setConnect(outputVariable, inputVariable);
                        }
                    }
                    i++;
                    line = content[i];

                }
                continue;

            }
            else if(line.contains("feedback")){
                while (i<content.length){
//                    System.out.println(line);

                    if(line.contains("}")) break;
                    line = line.split("//")[0];
                    if(line.trim() != ""){
                        Pattern pattern=Pattern.compile("\"");
                        String []r = pattern.split(line);

                        if(r.length >= 4){
                            String outputVariable = DEPFS.removeSpace(r[1]);
                            String inputVariable = DEPFS.removeSpace(r[3]);
                            setFeedback(outputVariable, inputVariable);
                        }
                    }
                    i++;
                    line = content[i];
                }
                continue;

            }
//            else if(line.contains("parameter")){
////                System.out.println(line);
//
//                parameter = new Parameter();
//                Pattern pattern=Pattern.compile("\\(.*?\\)");
//                Matcher matcher = pattern.matcher(line);
//                while(matcher.find()) {
//
//                    String name = matcher.group();
//                    if(matcher.group() != "") {
//                        int start = matcher.start();
//                        int end = matcher.end();
//                        name = line.substring(start + 1, end - 1);
//                        pattern = Pattern.compile(":");
//                        String[] filePath = pattern.split(name);
//                        if (filePath[0].trim().equals("file")) {
//                            parameter.setParameterFile(DEPFS.removeSpace(filePath[1].replace("\"","")));
//                        }
//                    }
//                }
//                i++;
//                line = content[i];
////                System.out.println(line);
//                while (i<content.length){
////                    System.out.println(line);
//
//                    if(line.contains("}")) break;
//                    line = line.split("//")[0];
//                    if(line.trim() != ""){
//                        pattern = Pattern.compile("\"");
//                        String []r = pattern.split(line);
//                        if(r.length >= 4){
//                            String name = DEPFS.removeSpace(r[1]);
//                            String value = "\"" + DEPFS.removeSpace(r[3]) + "\"";
//                            setParameter(name, value);
//
//                        }
//                        else if(r.length == 3){
//                            String name = DEPFS.removeSpace(r[1]);
//                            String value = DEPFS.removeSpace(r[2]);
//                            setParameter(name, value);
//
//                        }
//                    }
//                    i++;
//                    line = content[i];
//
//                }
//                continue;
//            }
            else if(line.contains("logging")){
                while (i<content.length){
//                    System.out.println(line);

                    line = line.split("//")[0];
                    if(line.trim() != ""){
                        Pattern pattern=Pattern.compile("\"");
                        String []r = pattern.split(line);
                        if(r.length == 4){
                            String name = DEPFS.removeSpace(r[1]);
                            String level = DEPFS.removeSpace(r[3]);
                            setLogging(name, level);
                        }
                    }
                    i++;
                    line = content[i];
                    if(line.contains("}")) break;
                }
                continue;
            }
            //iter while until if 还没有写
        }

    }

    /**
     * those set functions art to set all of the private fields
     * @param name, value
     */
    public void setModelParameter(String name, String value){
        modelParameterName = name;
        modelParameterValue = value;
    }
    public void setIter(String string){
        iter = string;
    }
    public void setWhile(String string){
        Sim_while = string;
    }
    public void setUntil(String string){
        Sim_until = string;
    }
    public void setIf(String string){
        Sim_if = string;
    }
    public void setComponent(String object, String name) {
        component.put(object, name);
    }
    public void setConnect(String outputVariable, String inputVariable){
        connect.put(outputVariable, inputVariable);
    }
    public void setFeedback(String outputVariable, String inputVariable){
        feedback.put(outputVariable, inputVariable);
    }
    public void setLogging(String name, String level){
        logging.put(name, level);
    }
    public void setParameter(String name, String value){
        parameter.setParameterConent(name, value);
    }

    /**
     * those get functions are to get all of the private fields
     * @return
     */
    public String getModelParameterName(){
        return modelParameterName;
    }
    public String getModelParameterValue() {
        return modelParameterValue;
    }
    public String getIter(){
        return iter;
    }
    public String getWhile(){
        return Sim_while;
    }
    public String getUntil(){
        return Sim_until;
    }
    public String getIf(){
        return Sim_if;
    }
    public Map getComponent(){
        return component;
    }
//    public Map getConnect(){
//        return connect;
//    }
    public Map getFeedback(){
        return feedback;
    }
    public Map getLogging(){
        return logging;
    }
    public Parameter getParameter(){
        return parameter;
    }
    public String toString(){
        String string = "model(";
        if(modelParameterValue != ""){
            string += modelParameterName+":" + "\"" + modelParameterValue + "\"";
        }
        string += "){" + "\n";
        //iter while until if 都没写
        String middle = "";
        if(!component.isEmpty()) {
            middle += "components {" + "\n";
            Iterator ita = null;
            ita = component.entrySet().iterator();
            while (ita.hasNext()) {
                Map.Entry entry = (Map.Entry) ita.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                middle += "\t\"" + key + "\"\t" + "\"" + value + "\"" + "\n";

            }
            middle += "}"+"\n";
        }
        if (!connect.isEmpty()){
            middle += connect.toString();
        }
        if (!feedback.isEmpty()){
            middle += "feedback {" + "\n";
            Iterator ita = null;
            ita = feedback.entrySet().iterator();
            while (ita.hasNext()) {
                Map.Entry entry = (Map.Entry) ita.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                middle += "\t\"" + key + "\"\t" + "\"" + value + "\"" + "\n";
            }
            middle += "}"+"\n";
        }
        if (!logging.isEmpty()){
            middle += "logging {" + "\n";
            Iterator ita = null;
            ita = logging.entrySet().iterator();
            while (ita.hasNext()) {
                Map.Entry entry = (Map.Entry) ita.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                middle += "\t\"" + key + "\"\t" + "\"" + value + "\"" + "\n";
            }
            middle += "}"+"\n";
        }
        if(!parameter.isEmpty()) {
            middle += parameter.toString();
//            System.out.println(parameter.toString());
        }
        string += SimFile.stdOut(middle) + "}\n";

        return string;
    }
}
