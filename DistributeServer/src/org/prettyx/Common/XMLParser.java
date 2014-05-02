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
/**
 * Parse the XML document or XML string to a List
 */

import java.io.InputStream;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

public class XMLParser {
    private static List elemList = new ArrayList();

    public static synchronized List parserXmlFromString(String xmlStr) throws Exception {
        elemList.clear();
        Document doc = (Document) DocumentHelper.parseText(xmlStr);
        Element rootElt = doc.getRootElement();
        getElementList(rootElt);
        return  elemList;
    }

    public static synchronized List parserXmlFromFile(String fileName) {
        elemList.clear();
        InputStream inputStream = Thread.currentThread().getClass().getResourceAsStream("/org/prettyx/Resource/" + fileName);
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(inputStream);
            Element rootElt = document.getRootElement();
            getElementList(rootElt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return elemList;
    }

    private static void getElementList(Element element) {
        List elements = element.elements();
        if (elements.size() == 0) {
            String xpath = element.getPath();
            String value = element.getTextTrim();
            elemList.add(new Leaf(xpath, value));
        } else {
            for (Iterator it = elements.iterator(); it.hasNext();) {
                Element elem = (Element) it.next();
                getElementList(elem);
            }
        }
    }

    public static String showListString(List elemList) {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = elemList.iterator(); it.hasNext();) {
            Leaf leaf = (Leaf) it.next();
            sb.append(leaf.getXpath()).append(" = ").append(leaf.getValue()).append("\n");
        }
        return sb.toString();
    }

}

class Leaf {
    private String xpath;
    private String value;

    public Leaf(String xpath, String value) {
        this.xpath = xpath;
        this.value = value;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}







