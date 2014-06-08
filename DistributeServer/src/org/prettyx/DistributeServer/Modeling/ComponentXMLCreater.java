package org.prettyx.DistributeServer.Modeling;

import java.io.File;

import org.prettyx.Common.DEPF;

public class ComponentXMLCreater {

    private String location;

    private String content;

    public ComponentXMLCreater(String url) {

        location = url;
        File file = new File(url);
        try {
            content = DEPF.readFile(file);
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
//        ComponentXMLCreater reflector = new ComponentXMLCreater(url);
//        reflector.showContent();
//
//    }




}