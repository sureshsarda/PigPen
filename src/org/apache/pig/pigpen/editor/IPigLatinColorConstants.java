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

import org.eclipse.swt.graphics.RGB;

/**
 * Some useful colour constants
 */
public interface IPigLatinColorConstants {

    public static RGB DEFAULT = new RGB(0, 0, 0);
    public static RGB STRING = new RGB(102, 153, 0);
    public static RGB COMMENT = new RGB(192, 192, 192);
    public static RGB KEYWORD = new RGB(127, 0, 85);
    public static RGB BUILTIN_FUNCTION = new RGB(255, 0, 33);
    public static RGB NUMBER = new RGB(51, 153, 204);
}
