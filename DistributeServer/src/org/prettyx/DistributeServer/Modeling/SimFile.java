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

public class SimFile {
    private Set oms_import = new HashSet();
    private Sim sim;

    public SimFile(){
        sim = new Sim();
    }
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
    public void setImport(String string){
        oms_import.add(string);
    }
    public void setSim(String string){
        sim = new Sim(string);
    }
    public Set getImport(){
        return oms_import;
    }
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
}
