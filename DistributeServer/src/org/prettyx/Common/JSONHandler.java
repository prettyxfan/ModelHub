// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.Common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XieFan on 8/10/14.
 */
public class JSONHandler {

    public void testArrayToJSON(){
        boolean[] boolArray = new boolean[]{true,false,true};
        JSONArray jsonArray = JSONArray.fromObject( boolArray );
        System.out.println( jsonArray );
        // prints [true,false,true]
    }


    //Collection对象转换成JSON
    public void testListToJSON(){
        List list = new ArrayList();
        list.add( "first" );
        list.add( "second" );
        JSONArray jsonArray = JSONArray.fromObject( list );
        System.out.println( jsonArray );
        // prints ["first","second"]
    }


    //字符串json转换成json， 根据情况是用JSONArray或JSONObject
    public void testJsonStrToJSON(){
        JSONArray jsonArray = JSONArray.fromObject( "['json','is','easy']" );
        System.out.println( jsonArray );
        // prints ["json","is","easy"]
    }


    //Map转换成json， 是用jsonObject
    public void testMapToJSON() {
        Map map = new HashMap();
        map.put("name", "json");
        map.put("bool", Boolean.TRUE);
        map.put("int", new Integer(1));
        map.put("arr", new String[]{"a", "b"});
        map.put("func", "function(i){ return this.arr[i]; }");

        JSONObject jsonObject = JSONObject.fromObject(map);
        System.out.println(jsonObject);

    }


}
