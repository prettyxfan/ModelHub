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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Connect {
    private Set keyToValue = new ListOrderedSet();

    public void put(String key, String value){
        keyToValue.add(key + "*" + value);
    }

    public boolean isEmpty(){
        if(keyToValue.isEmpty()) return true;
        else return false;
    }
    public String toString(){
        String string = "";

        if(!keyToValue.isEmpty()){
            string = "connect {\n";
            Iterator ita = null;
            ita = keyToValue.iterator();
            while (ita.hasNext()) {
                String str = (String) ita.next();
                String key = str.split("\\*")[0];
                String value = str.split("\\*")[1];
                string += "\t\"" + key + "\"\t\"" + value + "\"\n";
            }
        }
        string += "}" + "\n";

        return string;
    }}
