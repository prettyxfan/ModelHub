package org.prettyx.ComputeServer.Modeling;

import oms3.util.ProcessExecution;
import org.prettyx.Common.DEPF;
import org.prettyx.Common.LogUtility;
import org.prettyx.Common.StatusCodes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Wrapper for ProcessExecution
 *   oms-all.jar
 *   usage: java -jar oms-all.jar [-l <loglevel> ] [-r|-e|-d|-a|-s|-o] <simfile>
 *   Command line access to simulations.
 *   -r   run the <simfile>
 *   -e   edit parameter in <simfile>
 *   -o   open the last output folder in desktop <simfile>
 *   -d   document the <simfile>
 *   -a   run the <simfile> analysis
 *   -s   create SHA <simfile> digest
 *
 *   -mcp model classpath (jar files not specified in sim)
 *   -l <loglevel> set the log level:
 *   OFF|ALL|SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST
 */
public class OMSProcessExecution {

    private Thread task;
    private boolean ready = false;
    private StringBuffer processOutputBuffer;

    private ProcessExecution processExecution;

    public int setUpEnvironment(String omsHome, String workDir){

        String[] jvmOptions = new String[0];
        String javaHome = DEPF.javaHome();
        String javaClassPath = null;

        if (!new File(javaHome).exists()) {
            LogUtility.logUtility().log2err("Java Home Not Found! " + javaHome);
            return StatusCodes.JAVA_HOME_NOT_FOUND;
        }

        if (!new File(omsHome).exists()) {
            LogUtility.logUtility().log2err("OMS3 Home Not Found! " + omsHome);
            return StatusCodes.FILE_NOT_FOUND;
        }

        if (!new File(workDir).exists()) {
            LogUtility.logUtility().log2err("Work Dir Not Found! " + workDir);
            return StatusCodes.FILE_NOT_FOUND;
        }

        File omsall = new File(omsHome + "/" + OMSCore.depsJars()[0]);
        if (!omsall.exists()) {
            System.out.println(new StringBuilder().append("Not found: ").append(omsall).append("\n").toString());
            return StatusCodes.FILE_NOT_FOUND;
        }

        processExecution = new ProcessExecution(
                new File(
                        new StringBuilder().
                                append(javaHome).append(File.separator).
                                append("bin").append(File.separator).
                                append("java").toString()
                )
        );

        String separator = File.pathSeparatorChar == ';' ? "\"" : "";

        processExecution.setArguments(
                jvmOptions,
                new StringBuilder().
                        append(separator).
                        append("-Doms3.work=").append(workDir).
                        append(separator).toString(),
                "-cp",
                new StringBuilder().
                        append(separator).
                        append(jars(new File(omsHome), workDir, javaClassPath)).
                        append(separator).toString(),
                "oms3.CLI",
                "-l", "ALL",
                "-r",
                new StringBuilder().
                        append(separator).
                        append(new File(workDir + "/simulation.sim")).
                        append(separator).toString()
        );


        processOutputBuffer = new StringBuffer();
        processExecution.redirectOutput(new ProcessExecution.BasicWriter(){

            public void write(char[] cbuf, int off, int len) throws IOException {
                processOutputBuffer.append(new String(cbuf, off, len));
            }
        });
        processExecution.redirectError(new ProcessExecution.BasicWriter() {

            public void write(char[] cbuf, int off, int len) throws IOException {
                processOutputBuffer.append(new String(cbuf, off, len));
            }
        });

        ready = true;

        return StatusCodes.SUCCESS;
    }

    public String getProcessOutput(){

        return processOutputBuffer.toString();
    }

    public int runProcessExecution() {

        if (!ready) {
            return StatusCodes.NOT_READY;
        }

        task = new Thread(){
            @Override
            public void run() {

                try {
                    processExecution.exec();
                } catch (IOException e) {
                    LogUtility.logUtility().log2err("Run ProcessExecution Error! " + e.getMessage());
                }
            }
        };
        task.start();

        return StatusCodes.SUCCESS;
    }

    private String jars(File omsHome, String workDir, String java_classpath) {
        List list = new ArrayList();

        for (File file : omsHome.listFiles()) {
            if (file.getName().endsWith("jar")) {
                list.add(file.toString());
            }
        }
        if (workDir != null) {
            File omsLib = new File(workDir, "lib");
            if ((omsLib.exists()) && (omsLib.isDirectory())) {
                for (File file : omsLib.listFiles()) {
                    if (file.getName().endsWith("jar")) {
                        list.add(file.toString());
                    }
                }
            }
            File omsDist = new File(workDir, "dist");
            if ((omsDist.exists()) && (omsDist.isDirectory())) {
                for (File file : omsDist.listFiles()) {
                    if (file.getName().endsWith("jar")) {
                        list.add(file.toString());
                    }
                }
            }
        }

        if (java_classpath != null) {
            StringTokenizer t = new StringTokenizer(java_classpath, ";:");
            while (t.hasMoreTokens()) {
                list.add(t.nextToken());
            }
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String s = (String)list.get(i);
            b.append(s);
            if (i < list.size() - 1) {
                b.append(File.pathSeparatorChar);
            }
        }
        return b.toString();
    }
}
