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

import org.prettyx.Common.DEPFS;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sim {
    private String name = "";
    private String build = "";
    private Model model;
    private String efficiency = "";
    private Set resource = new HashSet();
    private String outputstrategy = "";
    private String summary = "";
    private String output = "";
    private Analysis analysis = null;

    public Sim(){
        model = new Model();
        analysis = new Analysis();
    }
    public Sim(String simContent){
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
        }
    }
    public void setName(String string){
        name = string;
    }
    public void setBuild(String string){
        build = string;
    }
    public void setModel(String string){
        model = new Model(string);
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


    public String toString(){
        String string = "sim(";
        if(name != ""){
            string += "name:" + "\"" + name + "\"";
        }
        string += "){" + "\n";
        //build efficiency outputstrategy summary output analysis 都没写

        string += model.toString();
        Iterator ita = null;
        ita = resource.iterator();
        while (ita.hasNext()) {
            String value = (String)ita.next();
            string += "resource " + "\"" + value + "\"" + "\n";
        }
        string += "}\n";

        return string;
    }
}

