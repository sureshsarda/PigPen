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
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Launches a pig job
 */
public class LaunchAction implements IWorkbenchWindowActionDelegate {

    /**
     * Entry point for launching the job
     */
    public void run(IAction action) {

        Display.getDefault().asyncExec(new Runnable() {

            public void run() {

                IWorkbenchPage page = PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow().getActivePage();

                // get the file
                IFile file = ActionHelper.GetSelectedFile(page.getSelection());

                if (file == null || !file.getName().endsWith("pig"))
                    return;

                ActionHelper helper = new ActionHelper();
                String id = helper.runLauncher(page, file);

                // track the console so that the kill feature knows which job to
                // kill
                Activator.putActionHelper(id, helper);
            }
        });
    }

    /**
     * Not implemented
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
     * Not implemented
     */
    public void dispose() {

    }

    /**
     * Not implemented
     */
    public void init(IWorkbenchWindow window) {
    }

}
