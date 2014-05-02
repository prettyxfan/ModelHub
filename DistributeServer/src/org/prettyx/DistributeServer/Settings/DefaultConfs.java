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
 * Only Use this Const in "Settings" Package
 */
public class DefaultConfs {
    /* Init Settings, Cannot Be Override */
    public static String KIND = "DistributeServer";

    public static String BasePath = "/.MIMS";
    public static String ConfigFileNAME = BasePath + "/DistributeServer.xml";
    public static String LogPath = BasePath + "/log";
    public static String LogFileName = LogPath + "/DistributeServer.log";


    /* Different Categories of Configurations */

    // Network Settings
    protected static int PORT = 8528;

    // Running Settings
    protected static String ServerName = "PrettyX_Distribute";



}
