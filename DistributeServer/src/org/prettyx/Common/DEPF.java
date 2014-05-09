package org.prettyx.Common;


import java.io.*;
import java.net.URL;

/**
 * Utility to Common Directories & Environment & Path & FileIO
 */
public class DEPF {

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
     * @return
     *      String
     * @throws java.io.IOException
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
     * @return
     *      String
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static String readFile(File fileName) throws IOException{

        FileReader fileReader = new FileReader(fileName);
        return readFile(new FileInputStream(fileName));
    }

    /**
     * Write File From OutputStream
     *
     * @param outputStream
     * @param content
     * @throws java.io.IOException
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
     * @param content
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void writeFile(File file, String content) throws FileNotFoundException, IOException{
        writeFile(new FileOutputStream(file), content);
    }

    /**
     * Copy File
     *
     * @param inputFile
     * @param outputFile
     * @throws java.io.IOException
     */
    public static void copyFile(File inputFile, File outputFile) throws IOException {
        String content = readFile(inputFile);
        writeFile(outputFile, content);
    }

    /**
     * Copy File
     *
     * @param inputStream
     * @param outputStream
     * @throws java.io.IOException
     */
    public static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException{
        String content = readFile(inputStream);
        writeFile(outputStream, content);
    }

    /**
     * Download File
     *
     * @param url
     * @param local
     * @throws java.io.IOException
     */
    static void downloadFromUrl(File url, File local) throws IOException{

        File tmp = local;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(url));
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(tmp));
        byte[] data = new byte[4096];
        int len = 0;
        while ((len = in.read(data)) >= 0) {
            bout.write(data, 0, len);
        }
        bout.close();
        in.close();
    }
}
