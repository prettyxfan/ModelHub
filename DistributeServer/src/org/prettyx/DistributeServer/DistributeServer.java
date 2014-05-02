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

import org.prettyx.Common.LogUtility;
import org.prettyx.DistributeServer.Settings.DefaultConfs;
import org.prettyx.DistributeServer.Settings.Settings;
import org.prettyx.DistributeServer.Settings.SettingsCenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DistributeServer {

    private void initializeSettings(){
        SettingsCenter settingsCenter = new SettingsCenter();
        settingsCenter.loadSettings();
    }

    public static void main(String[] args) {
        DistributeServer distributeServer = new DistributeServer();
        distributeServer.initializeSettings();

    }
}
