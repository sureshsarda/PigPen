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
package org.apache.pig.pigpen.preferences;

import org.apache.pig.pigpen.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Sets the default values for PigPen's preferences pane
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_PATH,
                System.getProperty("user.home"));
        store.setDefault(PreferenceConstants.P_PROPERTIES, "");
        store.setDefault(PreferenceConstants.P_CONFPATH,
                System.getProperty("user.home"));
        store.setDefault(PreferenceConstants.P_LOGPATH,
                System.getProperty("java.io.tmpdir"));
        store.setDefault(PreferenceConstants.P_SSHGATEWAY, "");
        store.setDefault(PreferenceConstants.P_CLASSPATH, 
                System.getProperty("java.class.path"));
    }
}
