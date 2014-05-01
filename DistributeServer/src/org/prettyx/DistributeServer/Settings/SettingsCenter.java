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

import org.prettyx.Common.StatusCodes;

import java.io.File;

/**
 * Control All Settings
 */
public class SettingsCenter {


    public int loadSettings(){

        return StatusCodes.SUCCESS;
    }


    /*
     * Check Whether "~/.MIMS/MIMS.xml" Exists.
     *
     */
    public int isConfigured(){

        File basePath = new File(System.getProperty("user.home") + DefaultConfs.BasePath);



        return StatusCodes.SUCCESS;
    }

}
