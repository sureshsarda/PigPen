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
package org.apache.pig.pigpen.proxies;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.pig.pigpen.Activator;
import org.apache.pig.pigpen.util.PigPenLog;
import org.apache.pig.pigpen.util.PluginConfiguration;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Loads up the <code>PigServer</code> codebase via reflection
 */
public class PigServerLoaderHelper {

    private static final String DEPENDENT_BRIDGE = "depends/bridge.jar";
    private static URLClassLoader l;

    private static void initClassLoader() {

        Object o = new Object();
        
        try {

            l = URLClassLoader.newInstance(getPigURL(), o.getClass()
                    .getClassLoader());

            Thread.currentThread().setContextClassLoader(l);

        } catch (MalformedURLException e) {

            PigPenLog.logError(e);
        }
    }

    /**
     * Does the loading
     * 
     * @param mode
     *            Mode of operation for the server ("LOCAL", "MAPREDUCE")
     * @return a <code>PigServer</code>
     */
    public static Object getPigServer(String mode) {

        try {

            if (l == null) {

                Object lock = new Object();
                synchronized (lock) {

                    initClassLoader();
                }
            }

            Class<?> clazz = l.loadClass("org.apache.pig.PigServer");

            Constructor<?> c = clazz
                    .getConstructor(new Class[] { String.class });
            Object server = c.newInstance(new Object[] { mode });

            return server;

        } catch (Throwable t) {

            PigPenLog.logError("Failed to load PigServer. (bad jar?", t);
            return null;
        }
    }

    /**
     * Does the loading
     * 
     * @return a <code>PigScriptParser</code>
     */
    public static Object getPigScriptParser(InputStream stream) {

        try {

            if (l == null) {

                Object lock = new Object();
                synchronized (lock) {

                    initClassLoader();
                }
            }

            Class<?> parserc = buildSubClass();

            Constructor<?> c = parserc
                    .getConstructor(new Class[] { InputStream.class });
            c.setAccessible(true);

            Object parser = c.newInstance(new Object[] { stream });
            return parser;

        } catch (Throwable t) {

            PigPenLog.logError("Failed to load GruntParser. (bad jar?)", t);
            return null;
        }
    }

    /**
     * returns the bridge proxying subclass for the core pigparser.
     * 
     * @return
     * @throws Exception
     */
    private static Class<?> buildSubClass() throws Exception {

        try {

            if (l == null) {
                
                Object lock = new Object();
                synchronized (lock) {

                    initClassLoader();
                }
            }

            
            Class<?> pppc = l.loadClass("org.apache.pig.pigpen.bridge.PigPenScriptParser");
            return pppc;

        } catch (Throwable t) {

            PigPenLog.logError("Failed to load GruntParser. (bad jar?)", t);
            return null;
        }
    }

    /**
     * Reflects out the PropertiesUtil unit
     * 
     * @return object of type <code>PropertiesUtil</code>
     */
    public static Object getPropertiesUtil() {

        try {

            Object lock = new Object();

            if (l == null) {
                synchronized (lock) {

                    initClassLoader();
                }
            }

            Class<?> clazz = l
                    .loadClass("org.apache.pig.impl.util.PropertiesUtil");

            Constructor<?> c = clazz.getConstructor(new Class[] {});
            Object util = c.newInstance(new Object[] {});

            return util;

        } catch (Throwable t) {

            PigPenLog.logError("Failed to load GruntParser. (bad jar?)", t);
            return null;
        }
    }

    /**
     * Reflects out the class of type org.apache.pig.Main
     * 
     * @return class of type <code>Main</code>
     */
    public static Class<?> getPigMainClass() {

        try {

            if (l == null) {
                
                Object lock = new Object();
                synchronized (lock) {

                    initClassLoader();
                }
            }

            Class<?> clazz = l.loadClass("org.apache.pig.Main");

            return clazz;

        } catch (Throwable t) {

            PigPenLog.logError("Failed to load GruntParser. (bad jar?)", t);
            return null;
        }
    }

    /**
     * gets a URL to the Pig Jar
     * 
     * @return a well formed URL
     * @throws MalformedURLException
     */
    public static URL[] getPigURL() throws MalformedURLException {

        String pigJarPath = PluginConfiguration.findContainingJar();
        String pigBridgePath = loadBridgeJar();
        
        URL[] urls = new URL[2];
        urls[0] = new File(pigJarPath).toURL();
        urls[1] = new File(pigBridgePath).toURL();
        return urls;
    }

    private static String loadBridgeJar() {
        
        try {

            IPath p = Activator.getDefault().getStateLocation();
            File jar = new File(p.toOSString() + DEPENDENT_BRIDGE.replace("depends", ""));

            System.out.println(p.toOSString() + DEPENDENT_BRIDGE.replace("depends", ""));
            
            if (!jar.exists()) {

                InputStream stream = FileLocator.openStream(
                        Activator.getDefault().getBundle()
                        , new Path(DEPENDENT_BRIDGE)
                        , false
                        );
                
                jar.createNewFile();

                FileOutputStream out = new FileOutputStream(jar);

                int bytein = stream.read();

                try {
                    while (bytein != -1) {

                        out.write(bytein);
                        bytein = stream.read();
                    }
                } finally {
                    if (out != null)
                        out.close();
                }
            }

            String bundlePath = jar.getAbsolutePath();
            return bundlePath;

        } catch (Exception e) {

            PigPenLog.logError(e);

            return null;
        }
        
    }
}
