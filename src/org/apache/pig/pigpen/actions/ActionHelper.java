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

import java.util.Iterator;

import org.apache.pig.pigpen.util.PigLauncher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

/**
 * Some helper routines for launching and killing jobs
 */
public class ActionHelper {

    private String file = null;
    private PigLauncher launcher;

    /**
     * Shows the pig launch console
     * 
     * @param page
     * @param file
     *            Used to build a unique ID for the console
     * @return a handle to the console
     * @throws PartInitException
     */
    public String runLauncher(IWorkbenchPage page, IFile file) {

        this.file = file.getName();
        PigLauncher launcher = new PigLauncher(file);
        this.launcher = launcher;
        launcher.run();
        return launcher.getConsoleId();
    }

    public void killLauncher() {
        launcher.kill();
    }

    /**
     * Gets a handle to the file associated with a user's selection
     * 
     * @param selection
     * @return a handle to the file
     */
    public static IFile GetSelectedFile(ISelection selection) {
        if (!(selection instanceof IStructuredSelection)) {

            return null;
        }

        Iterator<?> iter = ((IStructuredSelection) selection).iterator();
        if (!iter.hasNext())
            return null;
        Object elem = iter.next();
        if (!(elem instanceof IAdaptable))
            return null;
        return (IFile) ((IAdaptable) elem).getAdapter(IFile.class);
    }

    /**
     * 
     * @return
     */
    public String getFile() {
        return file;
    }

}