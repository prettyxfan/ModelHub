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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log Utilities
 *
 * Two Log Levels : INFO & ERROR
 *
 * Log output will be like this:
 *  [yyyy-MM-dd HH:mm:ss] [INFO/ERROR] [CLASS_NAME - METHOD_NAME] LOG_MESSAGE
 *
 */
public class LogUtility {

    /**
     * Single Pattern LogUtility Class to Handle All Log Operations
     */
    private static LogUtility logUtility = null;

    public LogUtility(){
        level = -1;
    }

    /**
     * Construct Method
     */
    public static synchronized LogUtility logUtility(){
        if (logUtility == null) {
            logUtility = new LogUtility();
        }
        return logUtility;
    }

    /**
     * -1 => Not Configured
     *  0 => No Log
     *  1 => Log to File
     *  2 => Log to Console
     *  3 => Log to Both File & Console
     */
    private int level ;
    public static int LOG_NO = 0;
    public static int LOG_TO_FILE = 1;
    public static int LOG_TO_STD = 2;
    public static int LOG_TO_ALL = 3;

    // Properties
    private PrintWriter stdOut ;
    private PrintWriter stdErr ;
    private FileWriter file;


    /**
     * Construct Method
     *
     * @param fileName
     *              path to storage the Log
     */
    public void configure(String fileName, int logLevel){

        if (level != -1) {
            try {
                file.close();
            } catch (IOException e){
                log2stdErr(e.getMessage());
            }
            stdOut.close();
            stdErr.close();
        }

        stdOut = new PrintWriter(System.out, true);
        stdErr = new PrintWriter(System.err, true);
        try {
            file = new FileWriter(fileName, true);
        } catch (IOException e){
            log2stdErr(e.getMessage());
        }

        level = logLevel;
    }

    /**
     * Deconstruct Method
     */
    protected void finalize() throws Throwable{

        super.finalize();

        try {
            file.close();
        } catch (IOException e){
            log2stdErr(e.getMessage());
        }
        stdOut.close();
        stdErr.close();
    }

    /**
     * wrapper for log2fileOut & log2stdOut
     *
     * @param message
     *              message to display
     */
    public void log2out(String message){

        switch (level) {
            case -1: {
                break;
            }
            case 0: {
                break;
            }
            case 1:{
                log2fileOut(message);
                break;
            }
            case 2:{
                log2stdOut(message);
                break;
            }
            case 3:{
                log2fileOut(message);
                log2stdOut(message);
                break;
            }
            default:{
                break;
            }
        }
    }

    /**
     * wrapper for log2fileErr & log2stdErr
     *
     * @param message
     *              message to display
     */
    public void log2err(String message){

        switch (level) {
            case -1: {
                break;
            }
            case 0: {
                break;
            }
            case 1:{
                log2fileErr(message);
                break;
            }
            case 2:{
                log2stdErr(message);
                break;
            }
            case 3:{
                log2fileErr(message);
                log2stdErr(message);
                break;
            }
            default:{
                break;
            }
        }
    }

    /**
     * Output Information to File
     *
     * @param message
     *              message to display
     */
    private void log2fileOut(String message){
        if (level == -1) {
            return;
        }
        try {
            file.write( prefixTime() + " [INFO] " +
                        "[" + formatStringToSeventy(Thread.currentThread().getStackTrace()[3].getClassName() + " - " +
                                                Thread.currentThread().getStackTrace()[3].getMethodName()) + "] " + "\t"
                        + message + "\n" );
            file.flush();
        } catch (IOException e) {
            log2stdErr(e.getMessage());
        }
    }

    /**
     * Output Error to File
     *
     * @param message
     *              message to display
     */
    private void log2fileErr(String message){
        if (level == -1) {
            return;
        }
        try {
            file.write( prefixTime() + " [ERROR]" +
                        "[" + formatStringToSeventy(Thread.currentThread().getStackTrace()[3].getClassName() + " - " +
                                                Thread.currentThread().getStackTrace()[3].getMethodName()) + "] " + "\t"
                            + message + "\n" );
            file.flush();
        } catch (IOException e) {
            log2stdErr(e.getMessage());
        }
    }

    /**
     * Output Information to StdOut
     *
     * @param message
     *              message to display
     */
    private void log2stdOut(String message){
        if (level == -1) {
            return;
        }
        stdOut.print( prefixTime() + " [INFO] " +
                    "[" + formatStringToSeventy(Thread.currentThread().getStackTrace()[3].getClassName() + " - " +
                                            Thread.currentThread().getStackTrace()[3].getMethodName()) + "] " + "\t"
                        + message + "\n" );
        stdOut.flush();

    }

    /**
     * Output Error to StdErr
     *
     * @param message
     *              message to display
     */
    private void log2stdErr(String message){
        if (level == -1) {
            return;
        }
        stdErr.print( prefixTime() + " [ERROR]" +
                    "[" + formatStringToSeventy(Thread.currentThread().getStackTrace()[3].getClassName() + " - " +
                                            Thread.currentThread().getStackTrace()[3].getMethodName()) + "] " + "\t"
                        + message + "\n" );
        stdErr.flush();
    }

    /**
     * Provide Prefix of Current Time
     *
     * @return String
     *              date will be format like this [yyyy-MM-dd HH:mm:ss]
     */
    private String prefixTime(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

        return dateFormat.format(now);
    }

    /**
     * Format String to Seventy Byte Length
     * @param source
     *              source String to format
     * @return String
     */
    private String formatStringToSeventy(String source){

        int targetLength = 70;

        int length = source.length();

        if (length <= targetLength) {
            int fillUp = targetLength - length;
            for (int i=0; i<fillUp; i++) {
                source =  " " + source;
            }
        } else {
            int cutUp = length - targetLength;
            source = source.substring(cutUp, length);
        }

        return source;
    }
}
