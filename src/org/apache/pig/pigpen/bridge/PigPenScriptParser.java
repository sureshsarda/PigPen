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
package org.apache.pig.pigpen.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.pig.tools.pigscript.parser.ParseException;
import org.apache.pig.tools.pigscript.parser.PigScriptParser;

/**
 * A proxy class built compatible with pig 0.7
 */
public class PigPenScriptParser extends PigScriptParser {
    
    public PigPenScriptParser(InputStream stream) {
        super(stream);
    }

    @Override
    protected void printAliases() throws IOException {
    }

    @Override
    protected void printHelp() {
    }

    @Override
    protected void processCD(String arg0) throws IOException {
    }

    @Override
    protected void processCat(String arg0) throws IOException {
    }

    @Override
    protected void processCopy(String arg0, String arg1) throws IOException {
    }

    @Override
    protected void processCopyFromLocal(String arg0, String arg1) {
    }

    @Override
    protected void processCopyToLocal(String arg0, String arg1)
            throws IOException {
    }

    @Override
    protected void processDescribe(String arg0) throws IOException {
    }

    @Override
    protected void processDump(String arg0) throws IOException {
    }

    @Override
    protected void processExplain(String arg0, String arg1, boolean arg2,
            String arg3, String arg4, List<String> arg5, List<String> arg6)
            throws IOException, ParseException {
    }

    @Override
    protected void processFsCommand(String[] arg0) throws IOException {
    }

    @Override
    protected void processIllustrate(String arg0) throws IOException {
    }

    @Override
    protected void processKill(String arg0) throws IOException {
    }

    @Override
    protected void processLS(String arg0) throws IOException {
    }

    @Override
    protected void processMkdir(String arg0) throws IOException {
    }

    @Override
    protected void processMove(String arg0, String arg1) throws IOException {
    }

    @Override
    protected void processPWD() throws IOException {
    }

    @Override
    protected void processPig(String arg0) throws IOException {
    }

    @Override
    protected void processRegister(String arg0) throws IOException {
    }

    @Override
    protected void processRemove(String arg0, String arg1) throws IOException {
    }

    @Override
    protected void processScript(String arg0, boolean arg1, List<String> arg2,
            List<String> arg3) throws IOException, ParseException {
    }

    @Override
    protected void processSet(String arg0, String arg1) throws IOException,
            ParseException {
    }

    @Override
    public void prompt() {
    }

    @Override
    protected void quit() {
    }
}
