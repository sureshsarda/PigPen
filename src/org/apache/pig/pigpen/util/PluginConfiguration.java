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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.pig.pigpen.Activator;
import org.apache.pig.pigpen.preferences.PreferenceConstants;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Some helper routines for handling settings
 */
public class PluginConfiguration {

    // map = "pair01;pair02;pair03"
    private final static String PAIR_SEPARATOR = ";";

    // pair = "key=>value"
    private final static String KEY_VALUE_SEPARATOR = "=>";

    private final static String BUNDLED_PIG = "bundled/pig-0.7.0-core.jar";

    /**
     * Gets the path to the pig library
     * 
     * @return the path to the library
     */
    public static String getJarPath() {

        IPreferenceStore store = Activator.getDefault().getPreferenceStore();

        String path = store.getString(PreferenceConstants.P_PATH);

        return path;
    }

    /**
     * Converts a <code>Map</code> of preferences into a concatenated string
     * 
     * @param items
     *            A Map of preferences
     * @return a concatenated string of preferences
     */
    public static String createList(Map<String, String> items) {
        final StringBuffer result = new StringBuffer();
        final Set<String> keys = items.keySet();
        for (String key : keys) {
            result.append(key);
            result.append(KEY_VALUE_SEPARATOR);
            result.append(items.get(key));
            result.append(PAIR_SEPARATOR);
        }
        return result.toString();
    }

    /**
     * Converts a concatenated string of preferences into a Map
     * 
     * @param stringList
     *            A concatenated string of preferences
     * @return a Map of preferences
     */
    public static Map<String, String> parseString(String stringList) {

        String[] pairs = stringList.split(PAIR_SEPARATOR);
        Map<String, String> map = new HashMap<String, String>();

        for (String pair : pairs) {

            if (pair.contains(KEY_VALUE_SEPARATOR)) {

                String[] tmp = pair.split(KEY_VALUE_SEPARATOR);

                if (tmp.length > 1) {

                    map.put(tmp[0], tmp[1]);
                    continue;
                }
                map.put(tmp[0], "");
            }
        }
        return map;
    }

    /**
     * Tries to find a user specified pig library. Fails back to the onboard pig
     * library
     * 
     * @return the path to a pig library
     */
    public static String findContainingJar() {

        String path = getJarPath();

        if (!"".equals(path)) {

            return path;

        } else {

            PigPenLog
                    .logInfo("No user specified jar file, using plugin bundle pig jar");

            return getBundledJar();
        }
    }

    /**
     * Returns the onboard pig-core jarfile's path
     * 
     * Unfortunately eclipse cannot resolve paths. We have to use the bundle's
     * "savearea" to stash our dynamic content.
     * 
     * @return a useable path in the "savearea"
     */
    public static String getBundledJar() {

        try {

            IPath p = Activator.getDefault().getStateLocation();
            File jar = new File("\""+ p.toOSString() + BUNDLED_PIG.replace("bundled", "") + "\"");

            if (!jar.exists()) {

                InputStream stream = FileLocator.openStream(
                        Activator.getDefault().getBundle()
                        , new Path(BUNDLED_PIG)
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
