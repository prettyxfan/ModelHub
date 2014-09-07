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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Inherit ClassLoader and Load Classes Dynamically
 */

public class ClassLoaderUtils extends ClassLoader {
    private InputStream classFile = null;
    List<Class<?>> serviceClassList = new LinkedList<Class<?>>();
    Map<URL, String> urlStringMap = new HashMap<URL, String>();

    /**
     * Constructor
     * @param filePath String file path
     * @throws java.lang.ClassNotFoundException
     */

    public ClassLoaderUtils(String filePath) throws ClassNotFoundException {
        super(getSystemClassLoader());
        findFile(new File(filePath), "");
        URLConnection connection;
        if(!urlStringMap.isEmpty()) {
            Iterator ita = null;
            ita = urlStringMap.entrySet().iterator();
            while (ita.hasNext()) {
                Map.Entry entry = (Map.Entry) ita.next();
                URL url = (URL) entry.getKey();
                String name = (String) entry.getValue();
                try {
                    connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();
                    this.classFile = inputStream;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte []bytes = new byte[1024];
                    int b = 0;

                    while ((b = inputStream.read(bytes)) != -1){
                        byteArrayOutputStream.write(bytes, 0, b);
                    }
                    byte[] data = byteArrayOutputStream.toByteArray();
                    defineClass(name, data,0,data.length);
                    inputStream.close();
                    byteArrayOutputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Find all the files of the giving directory path, including the files of the subdirectory
     * @param file file or directory
     * @param packageName the package name of the class file
     * @throws ClassNotFoundException
     */

    public void findFile(File file, String packageName) throws ClassNotFoundException {
        File[] files = file.listFiles();
        for (File fileElement : files){
            StringBuilder packageBuilder = new StringBuilder(packageName);
            if (fileElement.isDirectory()) {
                if (packageBuilder.length() > 0) {
                    packageBuilder.append(".");
                }
                packageBuilder.append(fileElement.getName());
                findFile(fileElement, packageBuilder.toString());
            } else if (fileElement.getName().endsWith(".class")){
                StringBuilder className = new StringBuilder(packageName);
                if (packageName != null && packageName.length() > 0) {
                    className.append(".");
                }
                className.append(fileElement.getName().replaceAll(".class", ""));
                try{
                    String name = className.toString();
                    urlStringMap.put(new URL("file://"+fileElement.getPath()),name);
                } catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * get all the class of the giving file path
     * @return class list
     * @throws ClassNotFoundException
     */

    public List<Class<?>> getServiceClassList() throws ClassNotFoundException {
        if(!urlStringMap.isEmpty()) {
            Iterator ita = null;
            ita = urlStringMap.entrySet().iterator();
            while (ita.hasNext()) {
                Map.Entry entry = (Map.Entry) ita.next();
                URL url = (URL) entry.getKey();
                String name = (String) entry.getValue();
                serviceClassList.add(Class.forName(name, true, this));
            }
        }
        return serviceClassList;
    }
}
