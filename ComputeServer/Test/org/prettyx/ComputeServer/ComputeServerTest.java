package org.prettyx.ComputeServer;

import oms3.util.ProcessExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.prettyx.Common.DEPF;
import org.prettyx.ComputeServer.Modeling.OMSCore;
import org.prettyx.ComputeServer.Modeling.OMSProcessExecution;
import org.prettyx.ComputeServer.Settings.DefaultConfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import static org.junit.Assert.*;

public class ComputeServerTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

//    @Test
//    public void testHello() throws Exception {
//        System.out.print("This is Hello Test");
//    }

    @Test
    public void testNew() throws Exception {


        OMSProcessExecution omsProcessExecution = new OMSProcessExecution();
        omsProcessExecution.setUpEnvironment(DEPF.userHome() + "/.MIMS/Runtime/OMS3", "/Users/PJW/Desktop/oms3/oms3");
        omsProcessExecution.runProcessExecution();

//        while (true) {
//            Scanner scanner = new Scanner(System.in);
//            byte b = scanner.nextByte();
//            if (b == 1) {
//                System.out.println(omsProcessExecution.getProcessOutput());
//            } else if (b == 0) {
//                System.exit(0);
//            }
//        }

    }


}