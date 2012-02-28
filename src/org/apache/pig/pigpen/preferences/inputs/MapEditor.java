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

import org.apache.pig.pigpen.preferences.PreferenceConstants;
import org.apache.pig.pigpen.preferences.Property;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * A property grid widget
 */
public abstract class MapEditor extends FieldEditor {

    /**
     * The list widget; <code>null</code> if none (before creation or after
     * disposal).
     */
    private Table table;

    /**
     * The button box containing the Add, Remove, Up, and Down buttons;
     * <code>null</code> if none (before creation or after disposal).
     */
    private Composite buttonBox;

    /**
     * The Add button.
     */
    private Button addButton;

    /**
     * The edit button
     */
    private Button editButton;
    /**
     * The Remove button.
     */
    private Button removeButton;

    /**
     * The selection listener.
     */
    private SelectionListener selectionListener;

    /**
     * Creates a new list field editor
     */
    protected MapEditor() {
    }

    /**
     * Creates a list field editor.
     * 
     * @param name
     *            the name of the preference this field editor works on
     * @param labelText
     *            the label text of the field editor
     * @param parent
     *            the parent of the field editor's control
     */
    protected MapEditor(String name, String labelText, Composite parent) {
        init(name, labelText);
        createControl(parent);
    }

    /**
     * Notifies that the Add button has been pressed.
     */
    private void addPressed() {
        setPresentsDefaultValue(false);
        Property prop = new Property();
        final PropertyInputDialog dialog = new PropertyInputDialog(getShell(),
                prop);
        if (dialog.open() != Window.OK)
            return;

        if (prop != null) {
            table.setItemCount(table.getItemCount() + 1);
            table.getItem(table.getItemCount() - 1).setText(
                    new String[] { prop.getKey(), prop.getValue() });
            selectionChanged();
        }
    }

    /**
     * Notifies that the Edit button has been pressed.
     */
    private void editPressed() {

        Property prop = new Property();

        int index = table.getSelectionIndex();
        if (index >= 0) {
            prop.setKey(table.getItem(index).getText(0));
            prop.setValue(table.getItem(index).getText(1));
        }
        final PropertyInputDialog dialog = new PropertyInputDialog(getShell(),
                prop);
        if (dialog.open() == Window.OK) {
            table.getItem(index).setText(
                    new String[] { prop.getKey(), prop.getValue() });
            selectionChanged();
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void adjustForNumColumns(int numColumns) {
        Control control = getLabelControl();
        ((GridData) control.getLayoutData()).horizontalSpan = numColumns;
        ((GridData) table.getLayoutData()).horizontalSpan = numColumns - 1;
    }

    /**
     * Creates the Add, Remove, Up, and Down button in the given button box.
     * 
     * @param box
     *            the box for the buttons
     */
    private void createButtons(Composite box) {
        addButton = createPushButton(box, "ListEditor.add");//$NON-NLS-1$
        editButton = createPushButton(box, "Edit"); // can't find
                                                    // documentation...
        removeButton = createPushButton(box, "ListEditor.remove");//$NON-NLS-1$
    }

    /**
     * Combines the given list of items into a single string. This method is the
     * converse of <code>parseString</code>.
     * <p>
     * Subclasses must implement this method.
     * </p>
     * 
     * @param items
     *            the list of items
     * @return the combined string
     * @see #parseString
     */
    protected abstract String createList(Map<String, String> items);

    /**
     * Helper method to create a push button.
     * 
     * @param parent
     *            the parent control
     * @param key
     *            the resource name used to supply the button's label text
     * @return Button
     */
    private Button createPushButton(Composite parent, String key) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(JFaceResources.getString(key));
        button.setFont(parent.getFont());
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        int widthHint = convertHorizontalDLUsToPixels(button,
                IDialogConstants.BUTTON_WIDTH);
        data.widthHint = Math.max(widthHint,
                button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
        button.setLayoutData(data);
        button.addSelectionListener(getSelectionListener());
        return button;
    }

    /**
     * Creates a selection listener.
     */
    public void createSelectionListener() {
        selectionListener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                Widget widget = event.widget;
                if (widget == addButton) {
                    addPressed();
                } else if (widget == removeButton) {
                    removePressed();
                } else if (widget == editButton) {
                    editPressed();
                } else if (widget == table) {
                    selectionChanged();
                }
            }
        };
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        Control control = getLabelControl(parent);
        GridData gd = new GridData();
        gd.horizontalSpan = numColumns;
        control.setLayoutData(gd);

        table = getTableControl(parent);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = numColumns - 1;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        table.setLayoutData(gd);

        buttonBox = getButtonBoxControl(parent);
        gd = new GridData();
        gd.verticalAlignment = GridData.BEGINNING;
        buttonBox.setLayoutData(gd);
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doLoad() {
        if (table != null) {
            String s = getPreferenceStore().getString(
                    PreferenceConstants.P_PROPERTIES);
            fillTableItems(s);
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doLoadDefault() {
        if (table != null) {
            table.removeAll();
            String s = PreferenceConstants.P_CONFPATH;
            s += "=>";
            s += getPreferenceStore().getDefaultString(
                    PreferenceConstants.P_CONFPATH);
            s += ";";
            s += PreferenceConstants.P_LOGPATH;
            s += "=>";
            s += getPreferenceStore().getDefaultString(
                    PreferenceConstants.P_LOGPATH);
            s += ";";
            s += PreferenceConstants.P_SSHGATEWAY;
            s += "=>";
            s += getPreferenceStore().getDefaultString(
                    PreferenceConstants.P_SSHGATEWAY);
            s += ";";
            s += PreferenceConstants.P_CLASSPATH;
            s += "=>";
            s += getPreferenceStore().getDefaultString(
                    PreferenceConstants.P_CLASSPATH);
            s += ";";
            fillTableItems(s);
        }
    }

    private void fillTableItems(String s) {
        final Map<String, String> map = parseString(s);

        for (String key : map.keySet()) {

            table.setItemCount(table.getItemCount() + 1);
            Property property = new Property();
            property.setKey(key);

            if (map.get(key) != null && map.get(key).trim() != "") {

                property.setValue(map.get(key));
            }

            table.getItem(table.getItemCount() - 1).setText(
                    new String[] { property.getKey(), property.getValue() });
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doStore() {
        final TableItem[] items = table.getItems();
        String s = "";
        for (TableItem item : items) {
            s += item.getText(0);
            s += "=>";
            s += item.getText(1);
            s += ";";
        }
        getPreferenceStore().setValue(PreferenceConstants.P_PROPERTIES, s);

    }

    /**
     * Returns this field editor's button box containing the Add, Remove, Up,
     * and Down button.
     * 
     * @param parent
     *            the parent control
     * @return the button box
     */
    public Composite getButtonBoxControl(Composite parent) {
        if (buttonBox == null) {
            buttonBox = new Composite(parent, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.marginWidth = 0;
            buttonBox.setLayout(layout);
            createButtons(buttonBox);
            buttonBox.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent event) {
                    addButton = null;
                    editButton = null;
                    removeButton = null;
                    buttonBox = null;
                }
            });

        } else {
            checkParent(buttonBox, parent);
        }

        selectionChanged();
        return buttonBox;
    }

    /**
     * Returns this field editor's list control.
     * 
     * @param parent
     *            the parent control
     * @return the list control
     */
    public Table getTableControl(Composite parent) {
        if (table == null) {
            table = new Table(parent, SWT.VIRTUAL | SWT.BORDER
                    | SWT.FULL_SELECTION);
            table.setLinesVisible(true);
            table.setSize(400, 70);
            table.setHeaderVisible(true);
            table.setFont(parent.getFont());
            table.addSelectionListener(getSelectionListener());
            table.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent event) {
                    table = null;
                }
            });

            final TableColumn column1 = new TableColumn(table, SWT.None);
            column1.setText("key");
            column1.setWidth(200);
            final TableColumn column2 = new TableColumn(table, SWT.None);
            column2.setText("value");
            column2.setWidth(400);

        } else {
            checkParent(table, parent);
        }
        return table;
    }

    /**
     * Creates and returns a new item for the list.
     * <p>
     * Subclasses must implement this method.
     * </p>
     * 
     * @return a new item String[2] => {key, value}
     */
    protected abstract Property getNewInputObject();

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    public int getNumberOfControls() {
        return 3;
    }

    /**
     * Returns this field editor's selection listener. The listener is created
     * if nessessary.
     * 
     * @return the selection listener
     */
    private SelectionListener getSelectionListener() {
        if (selectionListener == null) {
            createSelectionListener();
        }
        return selectionListener;
    }

    /**
     * Returns this field editor's shell.
     * <p>
     * This method is internal to the framework; subclassers should not call
     * this method.
     * </p>
     * 
     * @return the shell
     */
    protected Shell getShell() {
        if (addButton == null) {
            return null;
        }
        return addButton.getShell();
    }

    /**
     * Splits the given string into a list of strings. This method is the
     * converse of <code>createList</code>.
     * <p>
     * Subclasses must implement this method.
     * </p>
     * 
     * @param stringList
     *            the string
     * @return an array of <code>String</code>
     * @see #createList
     */
    protected abstract Map<String, String> parseString(String stringList);

    /**
     * Notifies that the Remove button has been pressed.
     */
    private void removePressed() {
        setPresentsDefaultValue(false);
        int index = table.getSelectionIndex();
        if (index >= 0) {
            table.remove(index);
            selectionChanged();
        }
    }

    /**
     * Notifies that the list selection has changed.
     */
    private void selectionChanged() {
        int index = table.getSelectionIndex();
        editButton.setEnabled(index >= 0);
        removeButton.setEnabled(index >= 0);
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    public void setFocus() {
        if (table != null) {
            table.setFocus();
        }
    }

    /*
     * @see FieldEditor.setEnabled(boolean,Composite).
     */
    @Override
    public void setEnabled(boolean enabled, Composite parent) {
        super.setEnabled(enabled, parent);
        getTableControl(parent).setEnabled(enabled);
        addButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
    }
}