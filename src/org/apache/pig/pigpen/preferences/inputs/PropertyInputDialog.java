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

import org.apache.pig.pigpen.preferences.Property;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A widget to display an input dialog
 */
public class PropertyInputDialog extends StatusDialog {

    private Text keyField;
    private Text valueField;
    private final Property property;

    /**
     * Constructor
     * 
     * @param parent
     * @param property
     */
    public PropertyInputDialog(final Shell parent, final Property property) {
        super(parent);
        this.property = property;
        createDialogArea(parent);
        setTitle("Add a property to PigPen");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);
        final Composite inner = new Composite(composite, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 2;
        inner.setLayout(layout);

        final Label keyLabel = new Label(inner, SWT.LEFT | SWT.WRAP);
        keyLabel.setEnabled(true);
        keyLabel.setText("Key: ");
        keyLabel.setLayoutData(gridDataForLabel(1));
        keyField = new Text(inner, SWT.SINGLE | SWT.BORDER);
        keyField.setEnabled(true);
        keyField.setLayoutData(gridDataForText(layout.numColumns - 1));
        keyField.setText(property.getKey() == null ? "" : property.getKey());

        final Label valueLabel = new Label(inner, SWT.LEFT | SWT.WRAP);
        valueLabel.setEnabled(true);
        valueLabel.setText("Value: ");
        valueLabel.setLayoutData(gridDataForLabel(1));
        valueField = new Text(inner, SWT.SINGLE | SWT.BORDER);
        valueField.setEnabled(true);
        valueField.setLayoutData(gridDataForText(layout.numColumns - 1));
        valueField.setText(property.getValue() == null ? "" : property
                .getValue());

        return composite;
    }

    @Override
    protected void okPressed() {
        property.setKey(keyField.getText().trim());
        property.setValue(valueField.getText().trim());
        super.okPressed();
    }

    private static GridData gridDataForLabel(int span) {
        GridData gd = new GridData(SWT.FILL, 0, true, true);
        gd.horizontalSpan = span;
        return gd;
    }

    private static GridData gridDataForText(int span) {
        GridData gd = new GridData(250, 12);
        gd.horizontalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalSpan = span;
        return gd;
    }
}
