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

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML Parser
 * provide two public static methods :parserXmlFromString & parserXmlFromFile
 * parse two kinds of xml : xml string & xml document
 *
 * warning: This class only can parse the xml(string or file) in which
 * has no more than one element having the same path
 */

public class XMLParser {

    private static Map elementMap = new HashMap<String, String>();

    /**
     * parse xml string
     *
     * @param xmlString
     *              string contains a xml
     * @return elementlist
     */

    public static synchronized Map parserXmlFromString(String xmlString) throws Exception {
        elementMap.clear();
        Document document = DocumentHelper.parseText(xmlString);
        Element rootElement = document.getRootElement();
        getElementList(rootElement);

        return elementMap;
    }

    /**
     * parse xml document
     *
     * @param fileName
     *              a xml file int the resource directory
     * @return elementlist
     */

    public static synchronized Map parserXmlFromResourceFile(String fileName) throws Exception {
        elementMap.clear();
        InputStream inputStream = Thread.currentThread().getClass().getResourceAsStream("/org/prettyx/Resource/" + fileName);
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element rootElt = document.getRootElement();
        getElementList(rootElt);
        inputStream.close();

        return elementMap;
    }


    /**
     * parse xml document
     *
     * @param filePath
     *              a xml file at anywhere
     * @return elementlist
     */

    public static synchronized Map parserXmlFromFile(String filePath) throws Exception {
        elementMap.clear();
        SAXReader reader = new SAXReader();
        Document document = reader.read(filePath);
        Element rootElement = document.getRootElement();
        getElementList(rootElement);

        return elementMap;
    }

    /**
     * traverse the xml document by recursion
     *
     * @param element
     *              the rootElement
     */
    @SuppressWarnings("unchecked")
    private static void getElementList(Element element) {
        List elements = element.elements();
        if (elements.size() == 0) {
            String xpath = element.getPath();
            String value = element.getTextTrim();
            elementMap.put(xpath, value);
        } else {
            for (Object object : element.elements() ) {
                Element local = (Element) object ;
                getElementList(local);
            }
        }
    }

}