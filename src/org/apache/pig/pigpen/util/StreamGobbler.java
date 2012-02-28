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

// CODE ADAPTED FROM http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html

import java.io.*;

/**
 * Munches stdout and stderr
 */
public class StreamGobbler implements Runnable {

    InputStream is;
    boolean isError;
    ConsoleLogger console;

    /**
     * Constructor
     * 
     * @param is
     *            Stream to munch
     * @param isError
     *            Is this stderr?
     * @param view
     *            The monitor to redirect to
     */
    public StreamGobbler(InputStream is, boolean isError, ConsoleLogger console) {
        this.is = is;
        this.isError = isError;
        this.console = console;
    }

    /**
     * Does the business of redirecting the stream
     */
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (isError)
                    console.error(line);
                else
                    console.info(line);
            }
        } catch (Throwable t) {
            PigPenLog.logError(
                    "Error capturing Pig output (was the job killed?)", t);
        }
    }
}
