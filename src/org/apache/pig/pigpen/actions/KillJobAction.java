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
package org.apache.pig.pigpen.actions;

import org.apache.pig.pigpen.Activator;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.console.IConsoleView;

/**
 * A delegate that kills the specified pig job
 */
public class KillJobAction implements IViewActionDelegate, Runnable {

    private IConsoleView view;
    private String name;

    /**
     * initializer
     */
    public void init(IViewPart view) {
        this.view = (IConsoleView) view;
    }

    /**
     * entry point from eclipse
     */
    public void run(IAction action) {

        name = view.getConsole().getName();
        Display.getDefault().asyncExec(this);
    }

    /**
     * Not implemented
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void run() {

        if (name != null) {
            ActionHelper helper = Activator.getActionHelper(name);

            if (helper != null)
                helper.killLauncher();

        }

    }

}
