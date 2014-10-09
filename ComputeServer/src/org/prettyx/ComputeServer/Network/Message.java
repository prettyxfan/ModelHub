// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: PengJingwen <pengjingwen1994@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.ComputeServer.Network;

public class Message {

    // Distribute - Browser
    public static final int D_B_LOGIN = 0;
    public static final int D_B_LOGOUT = 1;
    public static final int D_B_SIGN_UP = 2;
    public static final int D_B_GET_MODEL = 3;
    public static final int D_B_COMPILE = 4;
    public static final int D_B_RUN = 5;

    // Distribute - Compute
    public static final int D_C_RUN = 10;
    public static final int D_C_RESULT = 11;
    public static final int D_C_ANALYSIS = 12;

}
