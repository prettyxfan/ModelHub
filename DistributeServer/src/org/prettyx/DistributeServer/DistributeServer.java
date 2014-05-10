// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.DistributeServer;

import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;
import org.prettyx.DistributeServer.Network.DistributeServerHearken;
import org.prettyx.DistributeServer.Settings.SettingsCenter;

public class DistributeServer {

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
        DistributeServer distributeServer = new DistributeServer();
        distributeServer.initialize();

        DistributeServerHearken distributeServerHearken = new DistributeServerHearken(Integer.valueOf(distributeServer.settingsCenter.getSetting("Network", "Port")));
        distributeServerHearken.checkAndStart();

        distributeServer.test();
    }

    private void test(){

    }
}
