package org.prettyx.ComputeServer;


import net.sf.json.JSONObject;
import org.prettyx.Common.StatusCodes;
import org.prettyx.ComputeServer.Modeling.OMSProcessExecution;
import org.prettyx.ComputeServer.Network.ComputeServerHearken;
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

        ComputeServerHearken computeServerHearken = new ComputeServerHearken(Integer.valueOf(computeServer.settingsCenter.getSetting("Network", "Port")));
        computeServerHearken.checkAndStart();

        computeServer.test();
    }

    private void test(){
    }
}
