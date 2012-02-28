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
import java.lang.reflect.Method;

/**
 * Proxies for the <code>ParseException</code> thrown by the GruntParser
 */
public class ParseExceptionProxy extends Exception {

    private Object exception;
    private Class<?> clazz;

    private static final long serialVersionUID = 7115455908438217563L;

    /**
     * 
     * @param message
     */
    public ParseExceptionProxy(Object exception) {

        this.exception = exception;
        this.clazz = exception.getClass();
    }

    /**
     * returns the validation error token
     * 
     * @return an instance of <code>TokenProxy</code>
     */
    public TokenProxy getToken() {

        Field f;
        try {

            f = clazz.getField("currentToken");

            Object token = f.get(exception);
            return new TokenProxy(token);

        } catch (Exception e) {

            //sometimes there isn't a current token...
            return null;
        }

    }
    
    @Override
    public String getMessage() {

        Method m;
        try {

            m = clazz.getMethod("getMessage", new Class[] {});

            Object message = m.invoke(exception, new Object[] {});
            return message.toString();

        } catch (Exception e) {

            //sometimes there isn't a current token...
            return null;
        }

    }
}
