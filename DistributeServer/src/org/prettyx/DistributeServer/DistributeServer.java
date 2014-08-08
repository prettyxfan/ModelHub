// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.DistributeServer;

import org.prettyx.Common.DEPF;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;
import org.prettyx.DistributeServer.Network.DistributeServerHearken;
import org.prettyx.DistributeServer.Settings.SettingsCenter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DistributeServer {

    private SettingsCenter settingsCenter;

    /**
     * Initialize the Application Running Environment
     *
     * @return SUCCESS/FAIL
     */
    private int initialize(){
        // Initialize Settings
        settingsCenter = new SettingsCenter();
        if (settingsCenter.loadSettings() != StatusCodes.SUCCESS) {
            return StatusCodes.FAIL;
        }

        return StatusCodes.SUCCESS;
    }

    /**
     * The Main Application
     * @param args
     *          Command Line Parameters
     */
    public static void main(String[] args) {
        DistributeServer distributeServer = new DistributeServer();
        distributeServer.initialize();

        DistributeServerHearken distributeServerHearken = new DistributeServerHearken(Integer.valueOf(distributeServer.settingsCenter.getSetting("Network", "Port")));
        distributeServerHearken.checkAndStart();

        distributeServer.test();
    }


    public void test(){
        String absolutePathOfDB = this.settingsCenter.getSetting("Init", "RuntimeDatabasePath");

        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + absolutePathOfDB);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(5);

            ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table'\n ORDER BY name;");

            LogUtility.logUtility().log2out("First Table: " + resultSet.getString(1));

        } catch (Exception e){

            LogUtility.logUtility().log2err(e.getMessage());
        }
    }
}
