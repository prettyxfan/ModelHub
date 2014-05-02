// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.DistributeServer.Settings;

import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;

import java.io.File;

/**
 * Control All Settings
 */
public class SettingsCenter {


    public int loadSettings(){
        if (isConfigured() == StatusCodes.FAIL) {
            installConfigurations();
        }

        return StatusCodes.SUCCESS;
    }


    /*
     * Check "~/.MIMS" Directory. Create Files if not Exists;
     *
     * @return SUCCESS/FAIL
     */
    private int isConfigured(){

        // Check Directories
        File basePath = new File(System.getProperty("user.home") + DefaultConfs.BasePath);
        File logPath = new File(System.getProperty("user.home") + DefaultConfs.LogPath);

        if (basePath.isDirectory() && logPath.isDirectory()) {
            return StatusCodes.SUCCESS;
        }


        return StatusCodes.FAIL;
    }

    /**
     * When application start first time, should install default configurations to BasePath.
     *
     * @return SUCCESS/FAIL
     */
    private int installConfigurations(){

        System.out.println("Application starts first time.");
        System.out.println("Installing application support files will be in " + System.getProperty("user.home") + DefaultConfs.BasePath );

        File basePath = new File(System.getProperty("user.home") + DefaultConfs.BasePath);
        if (!basePath.mkdir()) {
            return StatusCodes.FAIL;
        }

        File logPath = new File(System.getProperty("user.home") + DefaultConfs.LogPath);
        if (!logPath.mkdir()) {
            return StatusCodes.FAIL;
        }


        System.out.println("Install successfully!");
        return StatusCodes.SUCCESS;
    }
}
