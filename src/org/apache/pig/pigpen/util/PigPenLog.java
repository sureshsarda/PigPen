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

import org.apache.pig.pigpen.Activator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * PigPen's internal log infrastructure
 */
public class PigPenLog {

    /**
     * Log an information message
     * 
     * @param message
     *            the message to log
     */
    public static void logInfo(String message) {
        log(IStatus.INFO, IStatus.OK, message, null);
    }

    /**
     * Log an error message
     * 
     * @param message
     *            the message to log
     */
    public static void logError(String message) {
        log(IStatus.ERROR, IStatus.OK, message, null);
    }

    /**
     * Log an error exception
     * 
     * @param exception
     *            the exception to log
     */
    public static void logError(Throwable exception) {
        logError("Unexpected Exception", exception);
    }

    /**
     * Log an error message and exception
     * 
     * @param message
     *            the message to log
     * @param exception
     *            the exception to log
     */
    public static void logError(String message, Throwable exception) {
        log(IStatus.ERROR, IStatus.OK, message, exception);
    }

    /**
     * write a structured status message to the log
     * 
     * @param severity
     *            A severity level
     * @param code
     *            A status code
     * @param message
     *            A message
     * @param exception
     *            An exception
     */
    public static void log(int severity, int code, String message,
            Throwable exception) {
        log(createStatus(severity, code, message, exception));
    }

    /**
     * Build a structured error
     * 
     * @param severity
     *            A severity level
     * @param code
     *            A status code
     * @param message
     *            A message
     * @param exception
     *            An exception
     * @return an IStatus instance
     */
    public static IStatus createStatus(int severity, int code, String message,
            Throwable exception) {
        return new Status(severity, Activator.PLUGIN_ID, code, message,
                exception);
    }

    /**
     * log a structured status message to the plugin's default log trap
     * 
     * @param status
     *            the status message to log
     */
    public static void log(IStatus status) {
        Activator.getDefault().getLog().log(status);
    }

}
