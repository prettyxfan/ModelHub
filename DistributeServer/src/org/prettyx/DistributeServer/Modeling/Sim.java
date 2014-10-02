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

import org.apache.commons.collections.set.ListOrderedSet;
import org.prettyx.Common.DEPFS;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sim is represent the element "sim" of the sim file
 * including other subelement
 */
public class Sim {
    private String name = "";
    private String build = "";
    private Model model;
    private String efficiency = "";
    private Set resource = new ListOrderedSet();
    private String outputstrategy = "";
    private Set summary = new ListOrderedSet();
    private String output = "";
    private String analysis = null;

    /**
     * default constructor with none input parameter
     */
    public Sim(){
        model = new Model();
//        analysis = new Analysis();
    }

    /**
     * Using the content of the sim element to create a sim object
     * @param simContent
     */
    public Sim(String simContent){
//        System.out.println(simContent);
        String [] content = simContent.split("\n");
        for(int i = 0; i<content.length; i++){
            String line = content[i];

            if(i == 0){
                Pattern pattern=Pattern.compile("\\(.*?\\)");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    String name = matcher.group();
                    if(matcher.group() != "") {
                        int start = matcher.start();
                        int end = matcher.end();
                        name = line.substring(start + 1, end - 1);
                        pattern = Pattern.compile(":");
                        String[] name1 = pattern.split(name);
                        if (name1[0].trim().equals("name")) {
                            setName(DEPFS.removeSpace(name1[1].replace("\"", "")));
                        }
                    }
                }
                continue;
            }
            else if(line.contains("resource")){
                Pattern pattern=Pattern.compile("\"");
                String []r = pattern.split(line);
                if(r[0].trim().equals("resource")){
                    setResource(DEPFS.removeSpace(r[1]));
                }
                continue;
            }
            else if(line.contains("outputstrategy")){
                Pattern pattern=Pattern.compile("\\(.*?\\)");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    String name = matcher.group();
                    if(matcher.group() != "") {
                        int start = matcher.start();
                        int end = matcher.end();
                        name = line.substring(start + 1, end - 1);
//                        System.out.println(name);
                        outputstrategy = name;
                    }
                }
                continue;
            }
            else if(line.contains("model")){
                String modelContent = "";
                int start = i;
                int end = 0;
                int count = 0;
                boolean found = false;
                while (i<content.length && !found){
                    line = content[i];
                    for(int j=0; j<line.length(); j++){
                        if(line.charAt(j) == '{'){
                            count++;
                        }
                        else if(line.charAt(j) == '}'){
                            count--;
                            if(count == 0){
                                found = true;
                                end = i;
                                break;
                            }
                        }
                    }
                    i++;
                }
                while(start <= end){
                    modelContent += content[start] + "\n";
                    start++;
                }
//                System.out.println(modelContent);

                setModel(modelContent);
            }
            else if(line.contains("efficiency")){
                Pattern pattern=Pattern.compile("\\(.*?\\)");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    String name = matcher.group();
                    if(matcher.group() != "") {
                        int start = matcher.start();
                        int end = matcher.end();
                        name = line.substring(start + 1, end - 1);
//                        System.out.println(name);
                        efficiency = name;
                    }
                }
                continue;
            }

            else if(line.contains("summary")){
                Pattern pattern=Pattern.compile("\\(.*?\\)");
                Matcher matcher = pattern.matcher(line);
                while(matcher.find()) {
                    String name = matcher.group();
                    if(matcher.group() != "") {
                        int start = matcher.start();
                        int end = matcher.end();
                        name = line.substring(start + 1, end - 1);
//                        System.out.println(name);
                        summary.add(name);
                    }
                }
                continue;
            }
            else if(line.contains("analysis")){
                String analysisContent = "";
                int start = i;
                int end = 0;
                int count = 0;
                boolean found = false;
                while (i<content.length && !found){
                    line = content[i];
                    for(int j=0; j<line.length(); j++){
                        if(line.charAt(j) == '{'){
                            count++;
                        }
                        else if(line.charAt(j) == '}'){
                            count--;
                            if(count == 0){
                                found = true;
                                end = i;
                                break;
                            }
                        }
                    }
                    i++;
                }
                while(start <= end){
                    analysisContent += content[start] + "\n";
                    start++;
                }
                analysis = analysisContent;

            }
        }
    }

    /**
     * those set functions art to set all of the private fields
     * @param string
     */
    public void setName(String string){
        name = string;
    }
    public void setBuild(String string){
        build = string;
    }
    public void setModel(String string){
        model = new Model(string);
    }
    public void setModel(Model model) {
        this.model = model;
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
        summary.add(string);
    }
    public void setOutput(String string){
        output = string;
    }
    public void setAnalysis(String string){
        /**
         * @TODO
         */
    }

    /**
     * those get functions are to get all of the private fields
     * @return
     */
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
    public Set getSummary(){
        return summary;
    }
    public String getOutput(){
        return output;
    }
    public String getAnalysis(){
        return  analysis;
    }


    public String toString(){

        String string = "OMS3.sim(";
        if(name != ""){
            string += "name:" + "\"" + name + "\"";
        }
        string += "){" + "\n";
        //build efficiency outputstrategy summary output analysis 都没写

        if(outputstrategy!=""){
            string += "\toutputstrategy("+outputstrategy+")\n";
        }
        string += SimFile.stdOut(model.toString());
        if(!resource.isEmpty()) {
            Iterator ita = null;
            ita = resource.iterator();
            while (ita.hasNext()) {
                String value = (String) ita.next();
                string += "resource " + "\"" + value + "\"" + "\n";
            }
        }
        if(efficiency!=""){
            string += "\tefficiency("+efficiency+")\n";
        }
        if(!summary.isEmpty()) {
            Iterator ita = null;
            ita = summary.iterator();
            while (ita.hasNext()) {
                String value = (String) ita.next();
                string += "\tsummary(" + value + ")\n";
            }
        }
        if(analysis!=null) {
            string += analysis;
        }

        string += "}\n";
        return string;
    }
}

