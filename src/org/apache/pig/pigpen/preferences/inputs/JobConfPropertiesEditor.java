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
package org.apache.pig.pigpen.preferences.inputs;

import java.util.Map;

import org.apache.pig.pigpen.util.PluginConfiguration;
import org.apache.pig.pigpen.preferences.Property;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

/**
 * The widget that holds PigPen's preferences
 */
public class JobConfPropertiesEditor extends MapEditor {

    /**
     * Constructor
     * 
     * @param name
     * @param labelText
     * @param parent
     */
    public JobConfPropertiesEditor(final String name, final String labelText,
            final Composite parent) {
        super(name, labelText, parent);
    }

    @Override
    protected String createList(Map<String, String> items) {
        return PluginConfiguration.createList(items);
    }

    @Override
    protected Property getNewInputObject() {
        
        Property property = new Property();
        PropertyInputDialog dialog = new PropertyInputDialog(getShell(),
                property);
        if (dialog.open() == Window.OK) {
            return property;
        } else {
            return null;
        }
    }

    @Override
    protected Map<String, String> parseString(String stringList) {
        return PluginConfiguration.parseString(stringList);
    }
}
