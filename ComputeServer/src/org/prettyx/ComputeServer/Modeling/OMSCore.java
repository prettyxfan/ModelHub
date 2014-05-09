package org.prettyx.ComputeServer.Modeling;

/**
 * Run OMS Simulation
 */
public class OMSCore {

    /**
     * Return Jars which OMS3 Depends
     * @return
     *      String
     */
    public static String[] depsJars(){

        String[] jars = {
                            "oms-all.jar",
                            "groovy-all-1.8.6.jar",
                            "jfreechart-1.0.12.jar",
                            "jcommon-1.0.15.jar",
                            "cpptasks-1.0b6-od.jar"
                        };
        return jars;
    }

    /**
     * Run Simulation With Sim File
     *
     * @param simFile
     *          Simulation File
     * @return
     *      SUCCESS/FAIL
     */
    public static int runSimulationWithSim(String simFile){

        return 0;
    }
}
