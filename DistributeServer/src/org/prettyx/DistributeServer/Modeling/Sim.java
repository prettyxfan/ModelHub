package org.prettyx.DistributeServer.Modeling;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by XieFan on 8/16/14.
 */
public class Sim {
    private String name = "";
    private String build = "";
    private Model model = null;
    private String efficiency = "";
    private Set resource = new HashSet();
    private String outputstrategy = "";
    private String summary = "";
    private String output = "";
    private Analysis analysis = null;

    public Sim(String simContent){

    }
    public void setName(String string){
        name = string;
    }
    public void setBuild(String string){
        build = string;
    }
    public void setModel(String string){
        /**
         * @TODO
         */
    }
    public void setEfficiency(String string){
        efficiency = string;
    }
    public void setResource(String string){
        resource.add(string);
    }
    public void setOutputstrategy(String string){
        outputstrategy = string;
    }
    public void setSummary(String string){
        summary = string;
    }
    public void setOutput(String string){
        output = string;
    }
    public void setAnalysis(String string){
        /**
         * @TODO
         */
    }
    public String getName(){
        return name;
    }
    public String getBuild(){
        return build;
    }
    public Model getModel(){
        return model;
    }
    public String getEfficiency(){
        return efficiency;
    }
    public Set getResource(){
        return resource;
    }
    public String getOutputstrategy(){
        return outputstrategy;
    }
    public String getSummary(){
        return summary;
    }
    public String getOutput(){
        return output;
    }
    public Analysis getAnalysis(){
        return  analysis;
    }
}
