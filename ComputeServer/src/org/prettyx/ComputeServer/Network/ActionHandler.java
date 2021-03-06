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
import org.java_websocket.WebSocket;
import org.prettyx.Common.DEPFS;
import org.prettyx.Common.LogUtility;
import org.prettyx.ComputeServer.Modeling.OMSProcessExecution;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;

/**
 * Action Handler
 * handle the message having different action
 * action is dived into login, logout, sign up, get model, run model
 *
 */

public class ActionHandler {

    public static void runModel(WebSocket connection, String data){

        String outputPath = "/tmp/" + UUID.randomUUID() + "/";
        String outputFile = "/tmp/" + UUID.randomUUID() + ".zip";

        try {

            DEPFS.decoderBase64File(data, outputFile);
            DEPFS.unzip(outputFile, outputPath);
            DEPFS.removeFile(outputFile);

            OMSProcessExecution omsProcessExecution = new OMSProcessExecution();
            omsProcessExecution.setUpEnvironment(DEPFS.userHome() + "/Documents/ModelHub/Runtime/OMS3", outputPath);
            omsProcessExecution.runProcessExecution();

            while (true) {
                if (omsProcessExecution.finished()) {
                    break;
                } else {
                    Thread.sleep(50);
                }
            }

            System.out.print(omsProcessExecution.getProcessOutput());
            String []strings = omsProcessExecution.getProcessOutput().split("\\n");
            for(int i=0; i<strings.length; i++){
                JSONObject jsonObject = JSONObject.fromObject("{action:11,data:'" + strings[i].replace("'","\"") + "'}");
                System.out.println(jsonObject.toString());
                connection.send(jsonObject.toString());
            }

            // TODO Return Data

            String analysisContent = DEPFS.readFile(new File(outputPath+"output/Sim/0000/output.csv"));
            System.out.println(analysisContent);
            JSONObject jsonObject = JSONObject.fromObject("{action:12,data:'" + analysisContent.replace("\n","#") + "'}");

            System.out.println(jsonObject.toString());
            connection.send(jsonObject.toString());
//            DEPFS.removeDirectoryAllFiles(outputPath);

        } catch (Exception e) {
            LogUtility.logUtility().log2out(e.getMessage());
        }
    }
}
