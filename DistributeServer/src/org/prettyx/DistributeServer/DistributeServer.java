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

public class DistributeServer {


    public static void main(String[] args) {

        LogUtility logUtility = new LogUtility( System.getProperty("user.home") + DefaultConfs.LogOutFileName,
                                                System.getProperty("user.home") + DefaultConfs.LogOutFileName );
        logUtility.log2fileErr("FUCK");


    }
}
