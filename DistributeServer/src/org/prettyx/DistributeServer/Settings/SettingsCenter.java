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
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;
import org.prettyx.Common.XMLParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Control All Settings
 *
 * settings will be storage like this:
 *      CATEGORY.ITEM => CONFIGURE    (e.g. Network.Port => 8528 )
 *
 * Can:
 *      load all settings storage in the configuration files
 *      obtain a specific setting associated with setting path
 *      automatic install default settings when application first start
 *
 */
public class SettingsCenter {

    // Storage Settings
    Map settingsMap;

    public SettingsCenter(){
        settingsMap = new HashMap<String, String>();
    }


    /**
     *  Load All Settings Storage in the Configuration Files
     *  Initialize LogUtility, Nothing will Happen if Invoke logUtility before this Method
     *
     * @return SUCCESS/FAIL/FILE_NOT_FOUND
     */
    @SuppressWarnings("unchecked")
    public int loadSettings(){
        if (isInstalled() == StatusCodes.FAIL) {
            if (install() == StatusCodes.FAIL) {
                return StatusCodes.FAIL;
            }
        }

        if (isEnvironmentOK() == StatusCodes.FAIL) {
            System.out.println("Error Happens, Check and Restart...");
            return StatusCodes.FAIL;
        }

        // Set the Fixed Init Settings
        Iterator itf = null;
        try {
            itf = DefaultConfs.fixSettingsMap.entrySet().iterator();
            while (itf.hasNext()){
                Map.Entry entry = (Map.Entry)itf.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                String[] keyBreak = key.split("/");

                if (keyBreak.length >= 4) {
                    key = keyBreak[2] + "." + keyBreak[3];
                }

                settingsMap.put(key, value);
            }
        } catch (NullPointerException e) {
            System.out.println("Load Settings Failed, Check and Restart...");
            return StatusCodes.FAIL;
        }

        //TODO NOT COMMON
        // Load Configurations
        Map configuresMap = null;
        try {
            configuresMap = XMLParser.parserXmlFromFile((String)DefaultConfs.fixSettingsMap.get("Init.DistributeServerConfigFilePath"));
        } catch (Exception e) {
            System.out.println("Load Conf File Failed, Check and Restart...");
            return StatusCodes.FAIL;
        }

        // Set the Alterable Settings
        Iterator ita = null;
        try {
            ita = configuresMap.entrySet().iterator();
            while (ita.hasNext()){
                Map.Entry entry = (Map.Entry)ita.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                String[] keyBreak = key.split("/");

                if (keyBreak.length >= 4) {
                    key = keyBreak[2] + "." + keyBreak[3];
                }

                settingsMap.put(key, value);
            }
        } catch (NullPointerException e) {
            System.out.println("Load Settings Failed, Check and Restart...");
            return StatusCodes.FAIL;
        }

        // Check Log File Exists
        File logFile = new File((String)DefaultConfs.fixSettingsMap.get("Init.LogPathPath"));
        if (!logFile.exists()) {
            return StatusCodes.FILE_NOT_FOUND;
        }

        // Init the LogUtility
        String logLevel = (String) settingsMap.get("Running.LogLevel");
        if (logLevel != null) {
            LogUtility.logUtility().configure((String)DefaultConfs.fixSettingsMap.get("Init.DistributeServerLogFilePath"),
                                                Integer.valueOf(logLevel));
            LogUtility.logUtility().log2out("Application is Starting!");
            LogUtility.logUtility().log2out("Configurations load successfully.");
        }

        return StatusCodes.SUCCESS;
    }

    //TODO NOT COMMON
    /**
     * Get Settings from DefaultConfsMapDistributeServer
     * @return string_value/null
     */
    public String getSetting(String category, String item){

        if (settingsMap.size() != 0) {

            String value = (String) settingsMap.get(category + "." + item);

            if (value == null || value.isEmpty()){
                value = (String) DefaultConfs.defaultSettingsMapDistributeServer.get(category + "." + item);
            }

            LogUtility.logUtility().log2out("GetSetting, key:" + category + "." + item + " value:" + value);

            if (value != null) {
                return value;
            }
        }

        return null;
    }

    /*
     * Check "~/.MIMS" Directory. Create Files if not Exists
     *
     * @return SUCCESS/FAIL
     */
    private int isInstalled(){

        //TODO NOT COMMON
        // Check Files
        File configFile = new File((String)DefaultConfs.fixSettingsMap.get("Init.DistributeServerConfigFilePath"));

        // Check Directories
        File basePath = new File((String)DefaultConfs.fixSettingsMap.get("Init.BasePath"));
        File logPath = new File((String)DefaultConfs.fixSettingsMap.get("Init.LogPathPath"));
        File runtimePath = new File((String)DefaultConfs.fixSettingsMap.get("Init.RuntimePathPath"));
        File runtimeOMSPath = new File((String)DefaultConfs.fixSettingsMap.get("Init.RuntimeOMSPathPath"));

        if (
            // Files
                configFile.isFile() &&
            // Directories
                basePath.isDirectory() &&
                logPath.isDirectory() &&
                runtimePath.isDirectory() &&
                runtimeOMSPath.isDirectory()
                ) {
            return StatusCodes.SUCCESS;
        }


        return StatusCodes.FAIL;
    }

    /**
     * Check System Runtime Environment
     *
     * @return SUCCESS/FAIL
     */
    private int isEnvironmentOK(){

        if (DEPFS.javaHome()  == null) {
            System.out.println("You need to install the latest JDK and set 'JAVA_HOME' to your JDK install directory.\n"
                    + "Please start again ...");
            return StatusCodes.FAIL;
        }

        try{
            String.class.getMethod("isEmpty", (Class[])null);
        } catch (Exception e) {
            System.out.println("You are using an older Java version, however JDK 1.6 is needed!\n" +
                    "Please install the right JDK, start again ...");
            return StatusCodes.FAIL;
        }

        return StatusCodes.SUCCESS;
    }

    /**
     * When application start first time, should install default configurations to Init_BasePath
     *
     * @return SUCCESS/FAIL
     */
    private int install(){

        System.out.println("Application starts first time.");
        System.out.println("Installing application support files will be in " +
                                (String)DefaultConfs.fixSettingsMap.get("Init.BasePath"));

        // Install Init_BasePath
        File basePath = new File((String)DefaultConfs.fixSettingsMap.get("Init.BasePath"));
        if (!basePath.isDirectory()) {
            if (!basePath.mkdirs()) {
                return StatusCodes.FAIL;
            }
        }

        // Install LogPath
        File logPath = new File((String)DefaultConfs.fixSettingsMap.get("Init.LogPathPath"));
        if (!logPath.isDirectory()) {
            if (!logPath.mkdirs()) {
                return StatusCodes.FAIL;
            }
        }

        // Install RuntimePath
        File runtimePath = new File((String)DefaultConfs.fixSettingsMap.get("Init.RuntimePathPath"));
        if (!runtimePath.isDirectory()) {
            if (!runtimePath.mkdirs()) {
                return StatusCodes.FAIL;
            }
        }
        File runtimeOMSPath = new File((String)DefaultConfs.fixSettingsMap.get("Init.RuntimeOMSPathPath"));
        if (!runtimeOMSPath.isDirectory()) {
            if (!runtimeOMSPath.mkdirs()) {
                return StatusCodes.FAIL;
            }
        }

        //TODO NOT COMMON

        // Install DistributeServer DefaultConfigFile
        try {

            InputStream inputStream = DEPFS.getResourceStream((String)
                    DefaultConfs.fixSettingsMap.get("Init.DistributeServerConfigFileNAME"));
            OutputStream outputStream = new FileOutputStream((String)
                                                DefaultConfs.fixSettingsMap.get("Init.DistributeServerConfigFilePath"));

            DEPFS.copyFile(inputStream, outputStream);

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return StatusCodes.FAIL;
        }

        // Install Database
        try {
            InputStream inputStream = DEPFS.getResourceStream((String)
                    DefaultConfs.fixSettingsMap.get("Init.RuntimeDatabaseName"));
            OutputStream outputStream = new FileOutputStream((String)
                                                DefaultConfs.fixSettingsMap.get("Init.RuntimeDatabasePath"));

            DEPFS.copyBinFile(inputStream, outputStream);

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return StatusCodes.FAIL;
        }

        // Install OMS Depends Jars
        try {

            String[] jars = {
                    "oms-all.jar",
                    "groovy-all-1.8.6.jar",
                    "jfreechart-1.0.12.jar",
                    "jcommon-1.0.15.jar",
                    "cpptasks-1.0b6-od.jar"
            };

            for (String jar : jars) {

                InputStream inputStream = DEPFS.getResourceStream("oms" + "/" + jar);
                OutputStream outputStream = new FileOutputStream((String)
                        DefaultConfs.fixSettingsMap.get("Init.RuntimeOMSPathPath") + "/" + jar);

                DEPFS.copyBinFile(inputStream, outputStream);

                inputStream.close();
                outputStream.close();
            }

        } catch (Exception e) {
            System.out.println("Install Failed!");
            e.printStackTrace();
            return StatusCodes.FAIL;
        }

        System.out.println("Install successfully!");
        return StatusCodes.SUCCESS;
    }
}
