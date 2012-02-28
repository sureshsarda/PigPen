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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.pig.pigpen.Activator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Provide functionality to log to the eclipse Console. Create an instance of
 * this class in any of your plugin classes.
 */
public class ConsoleLogger {

    private final String title;
    private MessageConsole messageConsole;

    public static enum Level {
        DEBUG, INFORMATION, WARNING, ERROR
    };

    public ConsoleLogger(final String messageTitle) {
        title = messageTitle;
    }

    public void setFocus() {
        messageConsole.activate();
    }

    public void debug(final String msg) {
        log(msg, Level.DEBUG);
    }

    public void info(final String msg) {
        log(msg, Level.INFORMATION);
    }

    public void warn(final String msg) {
        log(msg, Level.WARNING);
    }

    public void warn(final Throwable exception) {
        log(getStacktrace(exception), Level.WARNING);
    }

    public void error(final String msg) {
        log(msg, Level.ERROR);
    }

    public void error(final Throwable exception) {
        log(getStacktrace(exception), Level.ERROR);
    }

    public String getName() {
        return messageConsole.getName();
    }

    private void log(final String msg, final Level level) {
        if (msg == null)
            return;

        /*
         * if console-view in Java-perspective is not active, then show it and
         * then display the message in the console attached to it
         */
        if (!displayConsoleView()) {
            /*
             * If an exception occurs while displaying in the console, then just
             * display at least the same in a message-box
             */
            MessageDialog.openError(PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getShell(), "Error", msg);
            return;
        }

        /* print message on console */
        MessageConsoleStream stream = getNewMessageConsoleStream(level);

        MessageRunner runner = new MessageRunner();
        runner.setLevel(level);
        runner.setMessage(msg);
        runner.setStream(stream);
        Display.getDefault().asyncExec(runner);
    }

    private String getStacktrace(final Throwable exception) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

    private boolean displayConsoleView() {
        try {
            IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow();
            if (activeWorkbenchWindow != null) {
                IWorkbenchPage activePage = activeWorkbenchWindow
                        .getActivePage();
                if (activePage != null) {
                    activePage.showView(IConsoleConstants.ID_CONSOLE_VIEW,
                            null, IWorkbenchPage.VIEW_VISIBLE);

                }

            }
        } catch (PartInitException partEx) {
            return false;
        }
        return true;
    }

    private MessageConsoleStream getNewMessageConsoleStream(final Level level) {

        MessageConsoleStream msgConsoleStream = getMessageConsole()
                .newMessageStream();
        return msgConsoleStream;
    }

    private MessageConsole getMessageConsole() {

        createMessageConsoleStream(title);
        return messageConsole;
    }

    private void createMessageConsoleStream(final String title) {

        IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager()
                .getConsoles();
        for (IConsole console : consoles) {
            if (console.getName() == title)
                return;
        }

        try {
            messageConsole = new MessageConsole(title,
                    Activator.getImageDescriptor("icons/pig.png"));

        } catch (Exception e) {
            messageConsole = new MessageConsole(title, null);
        }
        ConsolePlugin.getDefault().getConsoleManager()
                .addConsoles(new IConsole[] { messageConsole });
    }

    public String getConsoleId() {
        return messageConsole.getName();
    }
}
