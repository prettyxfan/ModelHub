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

import org.prettyx.Common.DEPFS;

import java.util.HashMap;
import java.util.Map;

/**
 * Provide Global Default Settings for MINE
 * Only Use this Const in "Settings" Package
 */
public class DefaultConfs {
    /* Init Settings, Cannot Be Override */
    protected static Map fixSettingsMap = new HashMap<String, String>() {{

        put("Init.BasePath", DEPFS.userHome() + "/Documents/ModelHub");

        put("Init.DistributeServerConfigFileNAME", "DistributeServer.xml");
        put("Init.DistributeServerConfigFilePath", DEPFS.userHome() + "/Documents/ModelHub/DistributeServer.xml");
        put("Init.ComputeServerConfigFileNAME", "ComputeServer.xml");
        put("Init.ComputeServerConfigFilePath", DEPFS.userHome() + "/Documents/ModelHub/ComputeServer.xml");

        put("Init.LogPathName", "Log");
        put("Init.LogPathPath", DEPFS.userHome() + "/Documents/ModelHub/Log");
        put("Init.DistributeServerLogFileName", "DistributeServer.log");
        put("Init.DistributeServerLogFilePath", DEPFS.userHome() + "/Documents/ModelHub/Log/DistributeServer.log");
        put("Init.ComputeServerLogFileName", "ComputeServer.log");
        put("Init.ComputeServerLogFilePath", DEPFS.userHome() + "/Documents/ModelHub/Log/ComputeServer.log");

        put("Init.RuntimePathName", "Runtime");
        put("Init.RuntimePathPath", DEPFS.userHome() + "/Documents/ModelHub/Runtime");
        put("Init.RuntimeOMSPathName", "OMS3");
        put("Init.RuntimeOMSPathPath", DEPFS.userHome() + "/Documents/ModelHub/Runtime/OMS3");
        put("Init.RuntimeDatabaseName", "Database.sqlite");
        put("Init.RuntimeDatabasePath", DEPFS.userHome() + "/Documents/ModelHub/Runtime/Database.sqlite");
        put("Init.RuntimeUsersPathName", "Users");
        put("Init.RuntimeUsersPathPath", DEPFS.userHome() + "/Documents/ModelHub/Runtime/Users");
    }};


    /* Different Categories of Configurations */
    protected static Map defaultSettingsMapDistributeServer = new HashMap<String, String>(){{

        // Running Settings
        put("Running.ServerName", "PrettyX_Distribute");
        put("Running.LogLevel", "3");

        // Modeling
        put("Modeling.CLassSource", "dist");

        // Network Settings
        put("Network.Port", "8528");
    }};
    protected static Map defaultSettingsMapComputeServer = new HashMap<String, String>(){{

        // Running Settings
        put("Init.Kind", "DistributeServer");
        put("Running.ServerName", "PrettyX_Compute");
        put("Running.LogLevel", "3");

        // Network Settings
        put("Network.Port", "8529");
    }};
}
