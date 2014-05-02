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

import org.prettyx.DistributeServer.Settings.SettingsCenter;

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
