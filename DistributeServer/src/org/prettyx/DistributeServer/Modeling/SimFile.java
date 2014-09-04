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
import java.util.Set;

/**
 * SimFile is represent the sim file
 * including sim, test
 */
public class SimFile {
    private Set oms_import = new HashSet();
    private Sim sim;

    /**
     * default constructor with none input parameter
     */
    public SimFile(){
        sim = new Sim();
    }

    /**
     * Using the content of the sim file to create a simFile object
     * @param string
     */
    public SimFile(String string){
        String [] content = string.split("\n");
        for(int i= 0; i<content.length; i++){
            String line = content[i];
            if(line.contains("import")){    //simfile 中设置 import
                setImport(DEPFS.removeSpace(line));
                continue;
            }

            if(line.contains("sim")){

                String simContent = "";
                while (i<content.length){
                    simContent += content[i] + "\n";
                    i++;
                }

                setSim(simContent);
                break;
            }
            //build outputstrategy output summary efficiency analysis 还没有写
        }
    }

    /**
     * those set functions art to set all of the private fields
     * @param string
     */
    public void setImport(String string){
        oms_import.add(string);
    }

    public void setSim(String string){
        sim = new Sim(string);
    }

    public void setSim(Sim sim) {
        this.sim = sim;
    }

    public Set getImport(){
        return oms_import;
    }

    /**
     * those get functions are to get all of the private fields
     * @return
     */
    public Sim getSim(){
        return sim;
    }

    public String toString() {
        String string = "";
        Iterator ita = null;
        ita = oms_import.iterator();
        while (ita.hasNext()) {
            String value = (String)ita.next();
            string +=  value + "\n";
        }
        string += sim.toString();
        return string;
    }
    public static String stdOut(String string) {
        String []lines = string.split("\n");
        String returnString = "";
        for(int i=0;i<lines.length;i++) {
            returnString += "\t" +lines[i]+"\n";
        }
        return returnString;
    }
}
