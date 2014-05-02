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
public class DefaultSettings {
    /* Init Settings, Cannot Be Override */
    public static String Init_Kind = "DistributeServer";

    public static String Init_BasePath = "/.MIMS";
    public static String Init_DistributeServerConfigFileNAME = "DistributeServer.xml";
    public static String Init_DistributeServerConfigFilePath = Init_BasePath + "/" + Init_DistributeServerConfigFileNAME;
    public static String Init_LogPathName = "log";
    public static String Init_LogPathPath = Init_BasePath + "/" + Init_LogPathName;
    public static String Init_DistributeServerLogFileName = "DistributeServer.log";
    public static String Init_DistributeServerLogFilePath = Init_LogPathPath + "/" + Init_DistributeServerLogFileName;


    /* Different Categories of Configurations */

    // Running Settings
    protected static String Running_ServerName = "PrettyX_Distribute";
    protected static String Running_LogLevel = "3";

    // Network Settings
    protected static String Network_Port = "8528";





}
