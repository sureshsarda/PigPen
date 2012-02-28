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

import org.apache.pig.pigpen.editor.validation.PigPenValidationMarker;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * This is the main text editor component
 */
public class PigEditor extends TextEditor {

    private ColorManager colorManager;

    /**
     * Sets up the document partitioning and colouring
     */
    public PigEditor() {
        super();
        colorManager = new ColorManager();
        setSourceViewerConfiguration(new PigConfiguration(colorManager));
        setDocumentProvider(new PigDocumentProvider());
    }

    /**
     * Clean up
     */
    @Override
    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }

    /**
     * Validates the document when saved and requests marking up of syntax
     * errors
     */
    @Override
    protected void editorSaved() {
        super.editorSaved();
        validateAndMark();
    }

    /**
     * validates the script and marks it up with errors
     */
    protected void validateAndMark() {

        IDocument doc = getDocumentProvider().getDocument(getEditorInput());
        IFile file = (IFile) getEditorInput().getAdapter(IFile.class);

        new PigPenValidationMarker(doc, file);
    }
}
