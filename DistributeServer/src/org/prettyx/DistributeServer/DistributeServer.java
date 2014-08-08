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

import com.sun.java.util.jar.pack.*;
import org.prettyx.Common.DEPF;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;
import org.prettyx.DistributeServer.Network.DistributeServerHearken;
import org.prettyx.DistributeServer.Settings.SettingsCenter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

        String absolutePathOfdb = "/Users/XieFan/Documents/ModelHub/Runtime";
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlit.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + absolutePathOfdb + " Database.sqlite");
            String sql = "select * from Users;";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("username = " + resultSet.getString("username"));
                System.out.println("password = " + resultSet.getString("password"));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }

}
