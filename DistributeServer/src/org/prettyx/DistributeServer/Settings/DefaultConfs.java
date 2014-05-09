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
    public static String Init_Kind = "ComputeServer";

    public static String Init_BasePath = "/.MIMS";
    public static String Init_DistributeServerConfigFileNAME = "DistributeServer.xml";
    public static String Init_DistributeServerConfigFilePath = Init_BasePath + "/" + Init_DistributeServerConfigFileNAME;
    public static String Init_ComputeServerConfigFileNAME = "ComputeServer.xml";
    public static String Init_ComputeServerConfigFilePath = Init_BasePath + "/" + Init_ComputeServerConfigFileNAME;

    public static String Init_LogPathName = "Log";
    public static String Init_LogPathPath = Init_BasePath + "/" + Init_LogPathName;
    public static String Init_DistributeServerLogFileName = "DistributeServer.log";
    public static String Init_DistributeServerLogFilePath = Init_LogPathPath + "/" + Init_DistributeServerLogFileName;
    public static String Init_ComputeServerLogFileName = "ComputeServer.log";
    public static String Init_ComputeServerLogFilePath = Init_LogPathPath + "/" + Init_ComputeServerLogFileName;

    public static String Init_RuntimePathName = "Runtime";
    public static String Init_RuntimePathPath = Init_BasePath + "/" + Init_RuntimePathName;
    public static String Init_RuntimeOMSPathName = "OMS3";
    public static String Init_RuntimeOMSPathPath = Init_RuntimePathPath + "/" + Init_RuntimeOMSPathName;


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
        put("Running.ServerName", "PrettyX_Compute");
        put("Running.LogLevel", "3");

        // Network Settings
        put("Network.Port", "8529");
    }};
}
