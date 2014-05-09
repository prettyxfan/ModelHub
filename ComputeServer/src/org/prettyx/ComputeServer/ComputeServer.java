package org.prettyx.ComputeServer;


import org.prettyx.Common.DEPF;
import org.prettyx.Common.StatusCodes;
import org.prettyx.ComputeServer.Modeling.OMSProcessExecution;
import org.prettyx.ComputeServer.Settings.SettingsCenter;

import java.util.Scanner;

public class ComputeServer {

    private SettingsCenter settingsCenter;

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

    }
}
