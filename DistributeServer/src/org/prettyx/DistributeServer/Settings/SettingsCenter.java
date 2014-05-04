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
        if (isConfigured() == StatusCodes.FAIL) {
            if (installConfigurations() == StatusCodes.FAIL) {
                return StatusCodes.FAIL;
            }
        }

        // Set the Fixed Init Settings
        settingsMap.put("Init.Kind", DefaultSettings.Init_Kind);
        settingsMap.put("Init.BasePath", System.getProperty("user.home") +
                DefaultSettings.Init_BasePath);
        settingsMap.put("Init.DistributeServerConfigFileNAME", System.getProperty("user.home") +
                DefaultSettings.Init_DistributeServerConfigFileNAME);
        settingsMap.put("Init.DistributeServerConfigFilePath", System.getProperty("user.home") +
                DefaultSettings.Init_DistributeServerConfigFilePath);
        settingsMap.put("Init.LogPathName", System.getProperty("user.home") +
                DefaultSettings.Init_LogPathName);
        settingsMap.put("Init.LogPathPath", System.getProperty("user.home") +
                DefaultSettings.Init_LogPathPath);
        settingsMap.put("Init.DistributeServerLogFileName", System.getProperty("user.home") +
                DefaultSettings.Init_DistributeServerLogFileName);
        settingsMap.put("Init.DistributeServerLogFilePath", System.getProperty("user.home") +
                DefaultSettings.Init_DistributeServerLogFilePath);


        // Load Configurations
        Map configuresMap = null;
        try {
            configuresMap = XMLParser.parserXmlFromFile(System.getProperty("user.home") +
                                                        DefaultSettings.Init_DistributeServerConfigFilePath);
        } catch (Exception e) {
            LogUtility.logUtility().log2err(e.getMessage());
            return StatusCodes.FAIL;
        }

        // Set the Alterable Settings
        Iterator iterator = null;
        try {
            iterator = configuresMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry)iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                String[] keyBreak = key.split("/");

                if (keyBreak.length >= 4) {
                    key = keyBreak[2] + "." + keyBreak[3];
                }

                settingsMap.put(key, value);
            }
        } catch (NullPointerException e) {
            LogUtility.logUtility().log2err(e.getMessage());
            return StatusCodes.FAIL;
        }

        // Check Log File Exists
        File logFile = new File(System.getProperty("user.home") + DefaultSettings.Init_LogPathPath);
        if (!logFile.exists()) {
            return StatusCodes.FILE_NOT_FOUND;
        }

        // Init the LogUtility
        String logLevel = (String) settingsMap.get("Running.LogLevel");
        if (logLevel != null) {
            LogUtility.logUtility().configure(System.getProperty("user.home") +
                                                DefaultSettings.Init_DistributeServerLogFilePath,
                                                                                        Integer.valueOf(logLevel));
            LogUtility.logUtility().log2out("Application is Starting!");
            LogUtility.logUtility().log2out("Configurations load successfully.");
        }

        return StatusCodes.SUCCESS;
    }

    /**
     * Get Settings from defaultSettingsMap
     * @return string_value/null
     */
    public String getSetting(String category, String item){

        if (settingsMap.size() != 0) {

            String value = (String) settingsMap.get(category + "." + item);

            if (value == null || value.isEmpty()){
                value = (String) DefaultSettings.defaultSettingsMap.get(category + "." + item);
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
    private int isConfigured(){

        // Check Directories
        File basePath = new File(System.getProperty("user.home") + DefaultSettings.Init_BasePath);
        File logPath = new File(System.getProperty("user.home") + DefaultSettings.Init_LogPathPath);
        File runtimePath = new File(System.getProperty("user.home") + DefaultSettings.Init_RuntimePathPath);

        if (basePath.isDirectory() && logPath.isDirectory() && runtimePath.isDirectory()) {
            return StatusCodes.SUCCESS;
        }


        return StatusCodes.FAIL;
    }

    /**
     * When application start first time, should install default configurations to Init_BasePath
     *
     * @return SUCCESS/FAIL
     */
    private int installConfigurations(){

        System.out.println("Application starts first time.");
        System.out.println("Installing application support files will be in " +
                                System.getProperty("user.home") + DefaultSettings.Init_BasePath);

        // Install Init_BasePath
        File basePath = new File(System.getProperty("user.home") + DefaultSettings.Init_BasePath);
        if (!basePath.isDirectory()) {
            if (!basePath.mkdirs()) {
                return StatusCodes.FAIL;
            }
        }

        // Install LogPath
        File logPath = new File(System.getProperty("user.home") + DefaultSettings.Init_LogPathPath);
        if (!logPath.isDirectory()) {
            if (!logPath.mkdirs()) {
                return StatusCodes.FAIL;
            }
        }

        // Install RuntimePath
        File runtimePath = new File(System.getProperty("user.home") + DefaultSettings.Init_RuntimePathPath);
        if (!runtimePath.isDirectory()) {
            if (!runtimePath.mkdirs()) {
                return StatusCodes.FAIL;
            }
        }

        // Install DistributeServer DefaultConfigFile
        try {
            InputStream inputStream = Thread.currentThread().getClass().
                                            getResourceAsStream("/org/prettyx/Resource/" +
                                                    DefaultSettings.Init_DistributeServerConfigFileNAME);

            File outputFile = new File(System.getProperty("user.home") +
                                                    DefaultSettings.Init_DistributeServerConfigFilePath);
            outputFile.createNewFile();
            if (!outputFile.exists()) {
                return StatusCodes.FAIL;
            }
            OutputStream outputStream = new FileOutputStream(System.getProperty("user.home") +
                                                                DefaultSettings.Init_DistributeServerConfigFilePath);
            byte[] bytes = new byte[1];
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return StatusCodes.FAIL;
        }


        System.out.println("Install successfully!");
        return StatusCodes.SUCCESS;
    }
}
