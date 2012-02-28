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

import java.io.IOException;

import org.apache.pig.pigpen.util.ConsoleLogger.Level;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * A delegate to overcome eclipse threading constraints when writing to the
 * console
 */
public class MessageRunner implements Runnable {

    private String message;
    private MessageConsoleStream stream;
    private Level level;

    /**
     * The message to write to the console
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * The stream to pipe the message into
     * 
     * @param stream
     */
    public void setStream(MessageConsoleStream stream) {
        this.stream = stream;
    }

    /**
     * Level of severity for the message
     * 
     * @param level
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * does the business of logging the message to the console
     */
    public void run() {

        final int color;
        switch (level) {
        case WARNING:
            color = SWT.COLOR_DARK_MAGENTA;
            break;
        case ERROR:
            color = SWT.COLOR_RED;
            break;
        case DEBUG:
            color = SWT.COLOR_DARK_GRAY;
            break;
        case INFORMATION:
        default:
            color = SWT.COLOR_BLACK;
        }
        stream.setColor(Display.getDefault().getSystemColor(color));
        stream.println(message);

        try {
            stream.close();
        } catch (IOException e) {
            PigPenLog.logError(e);
        }
    }

}
