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
package org.apache.pig.pigpen.editor.validation;

/**
 * Declares a validation error
 */
public class PigValidationError {

    private String error;
    private int line;
    private int column;
    private int column2;

    public String getErrorMessage() {
        return error;
    }

    public void setErrorMessage(String error) {
        this.error = error;
    }

    public int getLineNumber() {
        return line;
    }

    public void setLineNumber(int line) {
        this.line = line;
    }

    public int getColumnNumber() {
        return column;
    }

    public void setColumnNumber(int column) {
        this.column = column;
    }

    public int getEndColumn() {
        return column2;
    }

    public void setEndColumn(int column2) {
        this.column2 = column2;
    }

    @Override
    public String toString() {
        return "Error on line " + line + ": " + error;
    }

}
