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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Parameter {
    private String parameterFile = "";
    private Map parameterConent = new HashMap();

    public void setParameterFile(String string){
        parameterFile = string;
    }
    public void setParameterConent(String name, String value){
        parameterConent.put(name, value);
    }
    public String getParameterFile(){
        return parameterFile;
    }
    public Map getParameterConent(){
        return parameterConent;
    }
    public Boolean isEmpty(){
        return parameterConent.isEmpty();
    }
    public String toString(){
        String string = "parameter(";
        if(parameterFile != ""){
            string += "file:" + "\"" + parameterFile + "\"";
        }
        string += "){" + "\n";
        Iterator ita = null;
        ita = parameterConent.entrySet().iterator();
        while (ita.hasNext()) {
            Map.Entry entry = (Map.Entry) ita.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            string += "\"" + key + "\" " + value + "\n";
        }
        string += "}" + "\n";

        return string;
    }
}
