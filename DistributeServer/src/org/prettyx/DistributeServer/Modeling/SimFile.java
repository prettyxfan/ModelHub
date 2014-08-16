package org.prettyx.DistributeServer.Modeling;

import org.prettyx.Common.DEPF;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by XieFan on 8/16/14.
 */
public class SimFile {
    private Set oms_import;
    private Sim sim;
    public SimFile(){
        oms_import = new HashSet();
    }
    public SimFile(String string){
        String [] content = string.split("\n");
        SimFile simFile = new SimFile();
        for(int i= 0; i<content.length; i++){
            String line = content[i];
            if(line.contains("import")){
                simFile.setImport(line);
                System.out.println(simFile.getImport());
                continue;
            }
            if(line.contains("sim")){
                String simContent = "";
                while (i<content.length){
                    simContent += content[i] + "\n";
                    i++;
                }
                simFile.setSim(simContent);
                System.out.println(simContent);
                break;
            }

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

}
