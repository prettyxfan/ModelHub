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

/**
 * Log Utilities
 */
public class LogUtility {

    private PrintWriter stdOut ;
    private PrintWriter stdErr ;
    private FileWriter fileOut;
    private FileWriter fileErr;

    public LogUtility(String p_fileOut, String p_fileError){
        stdOut = new PrintWriter(System.out, true);
        stdErr = new PrintWriter(System.err, true);
        try {
            fileOut = new FileWriter(p_fileOut, true);
            fileErr = new FileWriter(p_fileError, true);
        } catch (IOException e){
            e.printStackTrace();

        }

    }


    public void log2fileOut(String message){

    }

    public void log2fileErr(String message){
        try {
            fileErr.append(message);
            fileErr.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log2stdOut(String message){

    }

    public void log2stdErr(String message){

    }
}
