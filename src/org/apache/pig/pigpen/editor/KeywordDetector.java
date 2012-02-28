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
package org.apache.pig.pigpen.editor;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Helps SWT to detect a keyword in the script file
 */
public class KeywordDetector implements IWordDetector {

    /**
     * Indicates if the character is a keyword part
     */
    public boolean isWordPart(char character) {
        return isLetter(character) || Character.isDigit(character);
    }

    /**
     * Indicates if the character is a keyword start
     */
    public boolean isWordStart(char character) {
        return isLetter(character);
    }

    private boolean isLetter(char character) {
        return Character.isLetter(character) || character == '_';
    }
}
