package org.prettyx.ComputeServer;


import org.prettyx.Common.StatusCodes;
import org.prettyx.ComputeServer.Modeling.OMSProcessExecution;
import org.prettyx.ComputeServer.Settings.SettingsCenter;

import java.util.Scanner;

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

        computeServer.test();
    }

    private void test(){

        OMSProcessExecution omsProcessExecution = new OMSProcessExecution();
        omsProcessExecution.setUpEnvironment("/Users/XieFan/Documents/ModelHub/Runtime/OMS3", "/Users/XieFan/Documents/ModelHub/Runtime/Users/PengJingwen/Test");
        omsProcessExecution.runProcessExecution();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            byte b = scanner.nextByte();
            if (b == 1) {
                System.out.println(omsProcessExecution.getProcessOutput());
            } else if (b == 0) {
                System.exit(0);
            }
        }
    }
}
