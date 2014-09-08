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


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
     *          content
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeFile(File file, String content) throws FileNotFoundException, IOException{
        writeFile(new FileOutputStream(file), content);
    }


    /**
     * Write File From File
     *
     * @param in
     *          InputStream
     * @param out
     *          OutputStream
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void writeFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0)
        out.write(buffer, 0, len);

        in.close();
        out.close();
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
     * Copy directory
     *
     * @param sourceDirectory
     *              input
     * @param targetDirectory
     *              output
     * @throws IOException
     */

    public static void copyDirectory(String sourceDirectory,String targetDirectory) throws IOException{

        File[] file=(new File(sourceDirectory)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if(file[i].isFile()){
                File sourceFile = file[i];
                if(!sourceFile.getName().startsWith(".")) {
                    File targetFile = new File(new File(targetDirectory).getAbsolutePath() + File.separator + file[i].getName());
                    copyBinFile(sourceFile, targetFile);
                }

            }

            if(file[i].isDirectory()){
                String sourcedir = sourceDirectory + file[i].getName();
                String targetdir = targetDirectory + "/" + file[i].getName();
                createDirectory(targetdir);
                copyDirectory(sourcedir, targetdir);
            }
        }

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

    public static void removeDirectoryAllFiles(String path) {
        File f = new File(path);
        if(f.exists()) {
            File[] files = f.listFiles();
            if(files != null) {
                for(File file : files)
                    if(file.isDirectory()) {
                        removeDirectoryAllFiles(file.getPath());
                        file.delete();
                    }
                    else if(file.isFile()) {
                        file.delete();
                    }
            }
            f.delete();
        }
    }


    /**
     * Remove Directory
     *
     * @param path
     */

    public static void removeFile(String path){

        File file = new File(path);
        if (!file.isDirectory()) {
            file.delete();
        }
    }

    /**
     * zip directory to file
     * @param path
     *      file path
     * @param baseIndex
     *      base file path
     * @param out
     *      output zip file
     * @throws IOException
     */
    public static void zip(String path, int baseIndex, ZipOutputStream out) throws IOException{

        File file = new File(path);
        File[] files;
        if(file.isDirectory()){
            files = file.listFiles();
        }else{
            files = new File[1];
            files[0] = file;
        }

        for(File f:files){
            if(f.isDirectory()){

                String pathname = f.getPath().substring(baseIndex+1);

                out.putNextEntry(new ZipEntry(pathname + "/"));

                zip(f.getPath(), baseIndex, out);
            }else{

                String pathname = f.getPath().substring(baseIndex+1);

                out.putNextEntry(new ZipEntry(pathname));

                BufferedInputStream in = new BufferedInputStream(
                        new FileInputStream(f));
                int c;
                while((c=in.read()) != -1){
                    out.write(c);
                }
                in.close();
            }
        }
    }

    /**
     * unzip file
     * @param zipFile
     *          zip file path
     * @param outputPath
     *          output directory
     * @throws IOException
     */
    static public void unzip(String zipFile, String outputPath) throws IOException {

        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);

        String newPath = outputPath;

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()){

            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            //destFile = new File(newPath, destFile.getName());
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory()){

                BufferedInputStream is = new BufferedInputStream(zip
                        .getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[BUFFER];

                // write the current file to disk
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,BUFFER);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }

        }
    }

    /**
     * @param path
     *          filePath
     * @return  *
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);

    }

    /**
     * @param base64Code
     *          code
     * @param targetPath
     *          file path
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();

    }
}
