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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pig.pigpen.proxies.GruntParserProxy;
import org.apache.pig.pigpen.proxies.ParseExceptionProxy;
import org.apache.pig.pigpen.proxies.TokenProxy;
import org.apache.pig.pigpen.util.PigPenLog;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.MarkerUtilities;

/**
 * Marks syntax errors in a document
 */
public class PigPenValidationMarker {

    IFile file;
    IDocument document;

    public static final String ERROR_MARKER_ID = "pigscript.error";

    private List<PigValidationError> errorList = new ArrayList<PigValidationError>();

    /**
     * Parses and marks the syntax problems if found
     * 
     * @param doc
     * @param file
     */
    public PigPenValidationMarker(IDocument doc, IFile file) {

        this.document = doc;
        this.file = file;

        try {

            removeExistingMarkers();

            InputStream str = file.getContents();
            GruntParserProxy parser = new GruntParserProxy(str);

            int charCount = doc.getLength();
            for (int i = 0; i < charCount; i++) {

                try {
                    parser.parse();

                } catch (ParseExceptionProxy e) {

                    TokenProxy proxy = e.getToken().getNext();

                    handleError(
                            e
                            , proxy.getBeginLine()
                            , proxy.getBeginColumn()
                            , proxy.getEndColumn()
                            );

                    parser.getNextToken();
                }

            }

        } catch (Exception e) {

            // catch all
            PigValidationError err = new PigValidationError();
            err.setErrorMessage("script is invalid (scripts need to end with a new line)");
            err.setLineNumber(1);
            errorList.add(err);
            markError(err);

        }

    }

    /**
     * clear any previous error markers
     */
    protected void removeExistingMarkers() {
        try {
            file.deleteMarkers(ERROR_MARKER_ID, true, IResource.DEPTH_ZERO);
        } catch (CoreException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleError(Exception e, int lineNumber, int columnNumber,
            int endColumn) {

        PigValidationError error;
        error = nextError(e, lineNumber, columnNumber, endColumn);
        errorList.add(error);

    }

    protected PigValidationError nextError(Exception e, int lineNumber,
            int columnNumber, int endColumn) {

        String error = e.getMessage();

        PigValidationError validationError = new PigValidationError();
        validationError.setErrorMessage(error);
        validationError.setLineNumber(lineNumber);
        validationError.setColumnNumber(columnNumber);
        validationError.setEndColumn(endColumn);
        
        markError(validationError);

        return validationError;
    }

    private void markError(PigValidationError error) {
        Map<String, Object> map = new HashMap<String, Object>();

        int line = error.getLineNumber();

        map.put(IMarker.MESSAGE, error.getErrorMessage());
        map.put(IMarker.LOCATION, file.getFullPath().toString());
        try {

            map.put(IMarker.CHAR_START,
                    (document.getLineOffset(line - 1) + (error
                            .getColumnNumber() - 1)));
            map.put(IMarker.CHAR_END,
                    (document.getLineOffset(line - 1) + error.getEndColumn()));

        } catch (BadLocationException e) {

        }
        map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
        try {

            MarkerUtilities.createMarker(file, map, ERROR_MARKER_ID);

        } catch (CoreException e) {
            PigPenLog.logError(e);
        }
    }

    /**
     * gets a list of PigValidationError
     * 
     * @return the list of errors
     */
    public List<PigValidationError> getErrorList() {
        return errorList;
    }

}
