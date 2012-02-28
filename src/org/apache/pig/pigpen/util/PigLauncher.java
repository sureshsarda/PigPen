/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pig.pigpen.util;

import java.io.File;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.pig.pigpen.Activator;
import org.apache.pig.pigpen.util.PluginConfiguration;
import org.apache.pig.pigpen.preferences.PreferenceConstants;
import org.apache.pig.pigpen.proxies.PigServerLoaderHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Spins up a new JVM to run a pig job
 */
public class PigLauncher {

    private IFile file;
    private Process proc = null;
    private ConsoleLogger logger;

    /**
     * Constructor
     * 
     * @param file
     */
    public PigLauncher(IFile file) {
        this.file = file;
        logger = new ConsoleLogger("[PigPen] "
                + file.getName()
                + " ("
                + DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM,
                        DateFormat.MEDIUM).format(new Date()) + ")");
    }

    /**
     * Does the business of setting up the VM
     */
    public void run() {

        try {
            logger.info("Launching the job...");
            List<String> args = new LinkedList<String>();

            File jvm = new File(
                    new File(System.getProperty("java.home"), "bin"), "java");
            args.add(jvm.toString());

            args.add("-Xmx1g");

            String pigLocation = getPigLocation();
            String sep = System.getProperty("path.separator");
            
            IPreferenceStore store = Activator.getDefault()
            .getPreferenceStore();
            String stringList = store
            .getString(PreferenceConstants.P_PROPERTIES);
                Map<String, String> PrefProperties = PluginConfiguration
            .parseString(stringList);
            
            StringBuffer classPath = new StringBuffer().append(
                    PrefProperties.get(PreferenceConstants.P_CLASSPATH)
                    );
            
            if (classPath == null || classPath.length() == 0) {
                classPath.append(System.getProperty("java.class.path"));
            }
            
            if (pigLocation != null) {
                classPath.append(sep);
                classPath.append(pigLocation);
            }
            args.add("-classpath");
            
            String confPath = PrefProperties
                    .get(PreferenceConstants.P_CONFPATH);
            String logPath = PrefProperties.get(PreferenceConstants.P_LOGPATH);

            if (confPath == null || confPath.length() == 0) {

                logger.debug("Please set the variable "
                        + PreferenceConstants.P_CONFPATH
                        + " to point to the directory containing the configuration"
                        + " files in Pig preferences");
                return;
            }
            logger.info("Using the configuration from " + confPath);

            args.add(classPath.toString() + sep + confPath);

            // reflect the properties utility out of our pig backend
            Object util = PigServerLoaderHelper.getPropertiesUtil();
            Method m = util.getClass().getMethod("loadPropertiesFromFile",
                    new Class[] {});
            Properties properties = (Properties) m
                    .invoke(util, new Object[] {});

            String sshGateway = properties
                    .getProperty(PreferenceConstants.P_SSHGATEWAY);

            if (sshGateway != null && !"".equals(sshGateway)) {

                logger.info("Using SSH gateway = " + sshGateway);
                args.add("-Dssh.gateway=" + sshGateway);
            }

            args.add("org.apache.pig.Main");
            args.add("-exectype");
            args.add("mapreduce");

            String filename = file.getRawLocation().toPortableString();
            args.add("-f");
            args.add(filename);

            ProcessBuilder pb = new ProcessBuilder(args);

            if (logPath == null || logPath == "") {
                logPath = System.getProperty("java.io.tmpdir");
            }
            // set process working directory
            // to tmp otherwise run logs are dumped to the workspace.
            pb.directory(new File(logPath));

            proc = pb.start();

            // redirect stdout and stderr to us
            StreamGobbler errorGobbler = new StreamGobbler(
                    proc.getErrorStream(), true, logger);
            StreamGobbler outputGobbler = new StreamGobbler(
                    proc.getInputStream(), false, logger);

            // kick off the stream gobblers in separate threads
            Thread tstdout = new Thread(outputGobbler);
            Thread tstderr = new Thread(errorGobbler);
            tstdout.start();
            tstderr.start();

        } catch (Throwable t) {
            PigPenLog.logError("Error launching Pig", t);
            return;
        }
    }

    /**
     * Kill the VM. TODO: make this more elegant
     */
    public void kill() {
        if (proc != null) {
            proc.destroy();
            proc = null;
            logger.warn("Pig job killed");
        }

    }

    /**
     * Verifies that the pig library really is a pig library
     * 
     * @return the URL to the library
     */
    private String getPigLocation() {

        Class<?> clazz = PigServerLoaderHelper.getPigMainClass();
        CodeSource source = clazz.getProtectionDomain().getCodeSource();

        if (source != null) {

            return source.getLocation().getPath();

        } else {

            logger.error("org.apache.pig.Main doesn't exist (wrong jar file?)");
            return null;
        }
    }

    /**
     * Get the ID of the eclipse console that messages from the JVM are being
     * dumped to
     * 
     * @return the console's ID
     */
    public String getConsoleId() {
        return logger.getConsoleId();
    }
}