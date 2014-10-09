package org.prettyx.ComputeServer;


import org.java_websocket.WebSocket;
import org.prettyx.Common.DEPFS;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;
import org.prettyx.ComputeServer.Modeling.OMSProcessExecution;
import org.prettyx.ComputeServer.Network.ComputeServerHearken;
import org.prettyx.ComputeServer.Settings.SettingsCenter;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputeServer {

    private SettingsCenter settingsCenter;
    public static String absolutePathOfRuntimeUsers = null;

    /**
     * Initialize the Application Running Environment
     *
     * @return SUCCESS/FAIL
     */
    private int initialize(){
        // Initialize Settings
        settingsCenter = new SettingsCenter();
        if (settingsCenter.loadSettings() != StatusCodes.SUCCESS) {
            return StatusCodes.FAIL;
        }
        absolutePathOfRuntimeUsers = this.settingsCenter.getSetting("Init", "RuntimeUsersPathPath");
        return StatusCodes.SUCCESS;
    }

    /**
     * The Main Application
     * @param args
     *          Command Line Parameters
     */
    public static void main(String[] args) {
        ComputeServer computeServer = new ComputeServer();
        computeServer.initialize();

        ComputeServerHearken computeServerHearken = new ComputeServerHearken(Integer.valueOf(computeServer.settingsCenter.getSetting("Network", "Port")));
        computeServerHearken.checkAndStart();

//        computeServer.test();
    }

    private void test(){

            String outputPath = "/Users/XieFan/Documents/ModelHub/Runtime/Users/PengJingwen/Thornthwaite";
            try {

                OMSProcessExecution omsProcessExecution = new OMSProcessExecution();
                omsProcessExecution.setUpEnvironment(DEPFS.userHome() + "/Documents/ModelHub/Runtime/OMS3", outputPath);
                omsProcessExecution.runProcessExecution();

                while (true) {
                    if (omsProcessExecution.finished()) {
                        break;
                    } else {
                        Thread.sleep(50);
                    }
                }
//                String []result = omsProcessExecution.getProcessOutput().split("\\n");
//                for(String str:result) {
//                    Pattern pattern = Pattern.compile("^([0-9]{2}/[0-9]{2}.*)");
//                    Matcher matcher = pattern.matcher(str);
//                    boolean re= matcher.matches();
//
//                    if(!re){
//                        System.out.println(str);
//                    }
//                }

//                System.out.print(omsProcessExecution.getProcessOutput());


                // TODO Return Data

//            DEPFS.removeDirectoryAllFiles(outputPath);

            } catch (Exception e) {
                LogUtility.logUtility().log2out(e.getMessage());
            }
        }


}
