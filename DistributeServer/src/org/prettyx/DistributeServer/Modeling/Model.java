package org.prettyx.DistributeServer.Modeling;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XieFan on 8/16/14.
 */
public class Model {
    private String name = "";
    private String iter = "";
    private String Sim_while = "";
    private String Sim_until = "";
    private String Sim_if = "";
    private Map component = new HashMap();
    private Map connect = new HashMap();
    private Map feedback = new HashMap();
    private Map logging = new HashMap();
    private Map parameter = new HashMap();

    public void setClassname(String string){
        name = string;
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
        parameter.put(name, value);
    }

    public String getClassName(){
        return name;
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
    public Map getConnect(){
        return connect;
    }
    public Map getFeedback(){
        return feedback;
    }
    public Map getLogging(){
        return logging;
    }
    public Map getParameter(){
        return parameter;
    }

}
