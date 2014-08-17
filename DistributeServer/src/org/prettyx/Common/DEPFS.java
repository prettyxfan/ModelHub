// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.Common;


import org.prettyx.DistributeServer.Settings.DefaultConfs;

import java.io.*;
import java.net.URL;

/**
 * Utility to Common Directories & Environment & Path & FileIO & String
 */
public class DEPFS {

    /**
     * Return Java Home Path
     *
     * @return
     *      String
     */
    public static String javaHome(){

        String result = System.getenv("JAVA_HOME");
        if (result != null) {
            return result;
        }

        return System.getProperty("java.home");
    }

    /**
     * Return User Home Path
     *
     * @return
     *      String
     */
    public static String userHome(){

        String result = System.getenv("HOME");
        if (result != null) {
            return result;
        }

        return System.getProperty("user.home");
    }

    /**
     * Return InputStream of A Resource
     *
     * @param fileName
     *              fileName
     * @return
     *      InputStream
     */
    public static InputStream getResourceStream(String fileName){
        return Thread.currentThread().getClass().getResourceAsStream("/org/prettyx/Resource/" + fileName);
    }

    /**
     * Return InputStream of A Resource
     *
     * @param fileName
     *              fileName
     * @return
     *      URL
     */
    public static URL getResourceURL(String fileName){
        return Thread.currentThread().getClass().getResource("/org/prettyx/Resource/" + fileName);
    }

    /**
     * Read File From InputStream
     *
     * @param inputStream
     *                  input
     * @return
     *      String
     * @throws IOException
     */
    public static String readFile(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append('\n');
        }
        return stringBuffer.toString();
    }

    /**
     * Read File From File
     *
     * @param fileName
     *              fileName
     * @return
     *      String
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String readFile(File fileName) throws IOException{

        FileReader fileReader = new FileReader(fileName);
        return readFile(new FileInputStream(fileName));
    }

    /**
     * Write File From OutputStream
     *
     * @param outputStream
     *                  output
     * @param content
     *              content
     * @throws IOException
     */
    public static void writeFile(OutputStream outputStream, String content) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write(content);
        bufferedWriter.flush();
    }

    /**
     * Write File From File
     *
     * @param file
     *          file
     * @param content
     *              content
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeFile(File file, String content) throws FileNotFoundException, IOException{
        writeFile(new FileOutputStream(file), content);
    }

    /**
     * Copy File
     *
     * @param inputFile
     *              input
     * @param outputFile
     *              output
     * @throws IOException
     */
    public static void copyFile(File inputFile, File outputFile) throws IOException {
        String content = readFile(inputFile);
        writeFile(outputFile, content);
    }

    /**
     * Copy File
     *
     * @param inputStream
     *                  input
     * @param outputStream
     *                  output
     * @throws IOException
     */
    public static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException{
        String content = readFile(inputStream);
        writeFile(outputStream, content);
    }

    /**
     * Copy Bin File
     *
     * @param inputStream
     *                  input
     * @param outputStream
     *                  output
     * @throws IOException
     */
    public static void copyBinFile(InputStream inputStream, OutputStream outputStream) throws IOException{

        BufferedInputStream in = new BufferedInputStream(inputStream);
        BufferedOutputStream bout = new BufferedOutputStream(outputStream);
        byte[] data = new byte[4096];
        int len = 0;
        while ((len = in.read(data)) >= 0) {
            bout.write(data, 0, len);
        }
        bout.close();
        in.close();
    }

    /**
     * Copy Bin File
     *
     * @param inputFile
     *              input
     * @param outputFile
     *              output
     * @throws IOException
     */
    public static void copyBinFile(File inputFile, File outputFile) throws IOException{

        copyBinFile(new FileInputStream(inputFile), new FileOutputStream(outputFile));
    }

    /**
     * Remove the spaces before and after the string
     *
     * @param string
     *
     */
    public static String removeSpace(String string){
        int start = 0;
        int end = string.length() - 1;
        for(int i=0; i<string.length(); i++){
            char alpha = string.charAt(i);
            if(alpha != ' '){
                start = i;
                break;
            }
        }
        for(int i=string.length()-1; i>=0; i--){
            char alpha = string.charAt(i);
            if (alpha != ' '){
                end = i;
                break;
            }
        }
        return string.substring(start, end+1);
    }

    /**
     * Create Directory
     *
     * @param path
     */

    public static void createDirectory(String path){

        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * Remove Directory
     *
     * @param path
     */

    public static void removeDirectory(String path){

        File file = new File(path);
        if (file.isDirectory()) {
            file.delete();
        }
    }
}
