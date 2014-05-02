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

/**
 * Provide Global Default Settings for MINE
 * Use this Const in "Settings" Package
 */
public class DefaultConfs {
    /* Init Settings, Cannot Be Override */
    public static String KIND = "DistributeServer";

    public static String BasePath = "/.MIMS";
    public static String ConfigFileNAME = BasePath + "/MIMS.xml";
    public static String LogPath = BasePath + "/log";
    public static String LogOutFileName = LogPath + "/DistributeServer_Out.log";
    public static String LogErrFileName = LogPath + "/DistributeServer_Err.log";


    /* Different Categories of Configurations */

    // Network Settings
    public static int PORT = 8528;

    // Running Settings
    public static String ServerName = "PrettyX_Distribute";



}
