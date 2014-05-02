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
     * Single Pattern
     */
    private static LogUtility logUtility = null;

    public LogUtility(){
        configured = false;
    }
    public static synchronized LogUtility logUtility(){
        if (logUtility == null) {
            logUtility = new LogUtility();
        }
        return logUtility;
    }

    // Properties
    private PrintWriter stdOut ;
    private PrintWriter stdErr ;
    private FileWriter file;

    private boolean configured ;

    /**
     * Empty Construct Method
     */

    /**
     * Construct Method
     *
     * @param fileName
     *              path to storage the Log
     */
    public void configure(String fileName){
        stdOut = new PrintWriter(System.out, true);
        stdErr = new PrintWriter(System.err, true);
        try {
            file = new FileWriter(fileName, true);
        } catch (IOException e){
            log2stdErr(e.getMessage());
        }

        configured = true;
    }

    /**
     * Output Information to File
     *
     * @param message
     *              message to display
     */
    public void log2fileOut(String message){
        if (!configured) {
            return;
        }
        try {
            file.write( prefixTime() + " [INFO]\t" +
                      "[" + Thread.currentThread().getStackTrace()[2].getClassName() + " - " +
                            Thread.currentThread().getStackTrace()[2].getMethodName() + "] " + "\t\t"
                          + message + "\n" );
            file.flush();
        } catch (Exception e) {
            log2stdErr(e.getMessage());
        }
    }

    /**
     * Output Error to File
     *
     * @param message
     *              message to display
     */
    public void log2fileErr(String message){
        if (!configured) {
            return;
        }
        try {
            file.write( prefixTime() + " [ERROR]\t" +
                       "[" + Thread.currentThread().getStackTrace()[2].getClassName() + " - " +
                             Thread.currentThread().getStackTrace()[2].getMethodName() + "] " + "\t\t"
                           + message + "\n" );
            file.flush();
        } catch (Exception e) {
            log2stdErr(e.getMessage());
        }
    }

    /**
     * Output Information to StdOut
     *
     * @param message
     *              message to display
     */
    public void log2stdOut(String message){
        if (!configured) {
            return;
        }
        stdOut.print( prefixTime() + " [INFO]\t" +
                    "[" + Thread.currentThread().getStackTrace()[2].getClassName() + " - " +
                          Thread.currentThread().getStackTrace()[2].getMethodName() + "] " + "\t\t"
                        + message + "\n" );
        stdOut.flush();

    }

    /**
     * Output Error to StdErr
     *
     * @param message
     *              message to display
     */
    public void log2stdErr(String message){
        if (!configured) {
            return;
        }
        stdErr.print( prefixTime() + " [ERROR]\t" +
                    "[" + Thread.currentThread().getStackTrace()[2].getClassName() + " - " +
                          Thread.currentThread().getStackTrace()[2].getMethodName() + "] " + "\t\t"
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

}
