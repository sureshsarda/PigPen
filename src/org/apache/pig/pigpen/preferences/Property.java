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
package org.apache.pig.pigpen.preferences;

/**
 * A representation of a property key value pair of type <code>String</code>
 */
public class Property {

    private String key;

    private String value;

    /**
     * Get the key to this property
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Set the key to this property
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Get the value of this property
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value of this property
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * A custom representation of the property as a string
     */
    @Override
    public String toString() {
        return key + ":" + value;
    }
}
