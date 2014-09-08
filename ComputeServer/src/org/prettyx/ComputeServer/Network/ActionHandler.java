// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------
package org.prettyx.ComputeServer.Network;

import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.java_websocket.WebSocket;
import org.prettyx.Common.DEPFS;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.XMLParser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Action Handler
 * handle the message having different action
 * action is dived into login, logout, sign up, get model, run model
 *
 */

public class ActionHandler {

    public static void runModel(){

//        OMSProcessExecution omsProcessExecution = new OMSProcessExecution();
//        omsProcessExecution.setUpEnvironment("/Users/XieFan/Documents/ModelHub/Runtime/OMS3", "/Users/XieFan/Documents/ModelHub/Runtime/Users/PengJingwen/Test");
//        omsProcessExecution.runProcessExecution();
//
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            byte b = scanner.nextByte();
//            if (b == 1) {
//                System.out.println(omsProcessExecution.getProcessOutput());
//            } else if (b == 0) {
//                System.exit(0);
//            }
//        }

    }
}
