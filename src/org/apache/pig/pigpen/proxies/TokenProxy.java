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
package org.apache.pig.pigpen.proxies;

import java.lang.reflect.Field;

import org.apache.pig.pigpen.util.PigPenLog;

/**
 * Proxies for the lexical <code>Token</code> class
 */
public class TokenProxy {

    private Object token;

    protected TokenProxy(Object token) {
        this.token = token;
    }

    public int getBeginLine() {

        try {

            Field f = token.getClass().getField("beginLine");
            Object beginLine = f.get(token);
            return ((Integer) beginLine).intValue();

        } catch (Exception e) {

            PigPenLog.logError(e);
            return -1;
        }
    }

    public TokenProxy currentToken() {

        try {

            Field f = token.getClass().getField("currentToken");
            Object currentToken = f.get(token);

            if (currentToken == null) {
                return null;
            }
            return new TokenProxy(currentToken);

        } catch (Exception e) {

            PigPenLog.logError(e);
            return null;
        }

    }

    public TokenProxy getNext() {

        try {

            Field f = token.getClass().getField("next");
            Object nextToken = f.get(token);

            if (nextToken == null) {
                return null;
            }
            return new TokenProxy(nextToken);

        } catch (Exception e) {

            PigPenLog.logError(e);
            return null;
        }

    }

    public int getBeginColumn() {

        try {

            Field f = token.getClass().getField("beginColumn");
            Object beginColumn = f.get(token);
            return ((Integer) beginColumn).intValue();

        } catch (Exception e) {

            PigPenLog.logError(e);
            return -1;
        }
    }

    public int getEndColumn() {

        try {

            Field f = token.getClass().getField("endColumn");
            Object endColumn = f.get(token);
            return ((Integer) endColumn).intValue();

        } catch (Exception e) {

            PigPenLog.logError(e);
            return -1;
        }
    }
}
