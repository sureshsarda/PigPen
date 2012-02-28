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

import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.pig.pigpen.util.PigPenLog;

/**
 * Proxies for <code>GruntParser</code> to workaround Eclipse classloader
 * "features"
 * 
 */
public class GruntParserProxy {

    private Object gruntParser;
    private Class<?> clazz;

    /**
     * Instantiates a proxied grunt parser
     * 
     * @param stream
     */
    public GruntParserProxy(InputStream stream) {

        gruntParser = PigServerLoaderHelper.getPigScriptParser(stream);
        clazz = gruntParser.getClass();

        setInteractive(false);
    }

    private void setInteractive(boolean isInteractive) {

        try {

            Method m = clazz.getMethod("setInteractive",
                    new Class[] { boolean.class });
            m.invoke(gruntParser, new Object[] { isInteractive });

        } catch (Exception e) {

            PigPenLog.logError(e);
        }
    }

    /**
     * Proxies the <code>GruntParser.parse()</code> method
     * 
     * @throws ParseExceptionProxy
     *             , Exception
     */
    public void parse() throws ParseExceptionProxy, Exception {

        Method m;
        try {

            m = clazz.getMethod("parse", new Class[] {});
            m.invoke(gruntParser, new Object[] {});

        } catch (Exception e) {

            Class<?> clazzE = e.getClass();
            if (clazzE.getName() == "java.lang.reflect.InvocationTargetException") {

                try {

                    m = clazzE.getMethod("getTargetException", new Class[] {});
                    Object t = m.invoke(e, new Object[] {});

                    // sometimes thrown by the parser
                    if (t.getClass().getName() != "org.apache.pig.tools.pigscript.parser.ParseException"
                        && 
                        t.getClass().getName() != "org.apache.pig.tools.pigscript.parser.TokenMgrError"
                        ) {

                        PigPenLog.logError((Throwable) t);
                        return;
                    }

                    throw new ParseExceptionProxy(t);

                } catch (ParseExceptionProxy p) {

                    throw (p);

                } catch (Exception ee) {

                    throw (ee);
                }
            }
        }
    }

    /**
     * moves the parser on to the next lexical token in the event of validation
     * error
     */
    public void getNextToken() {

        try {

            Method m = clazz.getMethod("getNextToken", new Class[] {});
            m.invoke(gruntParser, new Object[] {});

        } catch (Exception e) {

            PigPenLog.logError(e);
        }

    }
}
