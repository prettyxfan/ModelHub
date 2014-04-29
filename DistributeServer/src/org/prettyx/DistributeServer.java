// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx;

import org.prettyx.Common.MIMS;
import org.prettyx.Network.DistributeServerHearken;

import java.io.InputStream;

public class DistributeServer {


    public static void main(String[] args) {


        DistributeServerHearken distributeServer = new DistributeServerHearken(MIMS.PORT);
        distributeServer.start();
    }
}
