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

import java.util.HashMap;
import java.util.Map;

/**
 * Provide Global Default Settings for MINE
 * Only Use this Const in "Settings" Package
 */
public class DefaultConfs {
    /* Init Settings, Cannot Be Override */
    protected static Map fixSettingsMap = new HashMap<String, String>() {{

        put("Init.BasePath", "/.MIMS");

        put("Init.DistributeServerConfigFileNAME", "DistributeServer.xml");
        put("Init.DistributeServerConfigFilePath", "/.MIMS/DistributeServer.xml");
        put("Init.ComputeServerConfigFileNAME", "ComputeServer.xml");
        put("Init.ComputeServerConfigFilePath", "/.MIMS/ComputeServer.xml");

        put("Init.LogPathName", "Log");
        put("Init.LogPathPath", "/.MIMS/Log");
        put("Init.DistributeServerLogFileName", "DistributeServer.log");
        put("Init.DistributeServerLogFilePath",  "/.MIMS/Log/DistributeServer.log");
        put("Init.ComputeServerLogFileName", "ComputeServer.log");
        put("Init.ComputeServerLogFilePath", "/.MIMS/Log/ComputeServer.log");

        put("Init.RuntimePathName", "Runtime");
        put("Init.RuntimePathPath", "/.MIMS/Runtime");
        put("Init.RuntimeOMSPathName", "OMS3");
        put("Init.RuntimeOMSPathPath", "/.MIMS/Runtime/OMS3");
    }};


    /* Different Categories of Configurations */
    protected static Map defaultSettingsMapDistributeServer = new HashMap<String, String>(){{

        // Running Settings
        put("Running.ServerName", "PrettyX_Distribute");
        put("Running.LogLevel", "3");

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
