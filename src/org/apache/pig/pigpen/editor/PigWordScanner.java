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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.pig.pigpen.Activator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;

/**
 * Defines the rules for keywords etc. in the script file
 */
public class PigWordScanner extends RuleBasedScanner {

    private static Set<String> KEYWORDS = new HashSet<String>();
    private static Set<String> BUILTIN_FUN = new HashSet<String>();

    private final ColorManager manager;

    static {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    org.eclipse.core.runtime.FileLocator.openStream(Activator
                            .getDefault().getBundle(), new Path(
                            "data/keywords.txt"), false)));

            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    KEYWORDS.add(line);
                }
            }
            reader.close();

            BufferedReader reader2 = new BufferedReader(new InputStreamReader(
                    org.eclipse.core.runtime.FileLocator.openStream(Activator
                            .getDefault().getBundle(), new Path(
                            "data/builtin_functions.txt"), false)));
            while ((line = reader2.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    BUILTIN_FUN.add(line);
                }
            }
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up our various colouring and highlighting rules
     * 
     * @param manager
     */
    public PigWordScanner(final ColorManager manager) {
        this.manager = manager;

        IToken commentToken = new Token(new TextAttribute(
                manager.getColor(IPigLatinColorConstants.COMMENT), null,
                SWT.ITALIC));
        IToken stringToken = new Token(new TextAttribute(
                manager.getColor(IPigLatinColorConstants.STRING)));
        IToken numberToken = new Token(new TextAttribute(
                manager.getColor(IPigLatinColorConstants.NUMBER)));

        final IRule[] rules = new IRule[5];

        // Add generic whitespace rule.
        rules[0] = new WhitespaceRule(new PigWhitespaceDetector());

        // only allowed one WordRule
        WordRule wordRule = addWordRules();
        rules[1] = wordRule;

        rules[2] = new EndOfLineRule("--", commentToken); //$NON-NLS-1$
        rules[3] = new NumberRule(numberToken);
        rules[4] = new SingleLineRule("'", "'", stringToken, '\\'); //$NON-NLS-2$ //$NON-NLS-1$

        setRules(rules);
    }

    private WordRule addWordRules() {
        WordRule wordRule = new WordRule(new KeywordDetector(),
                getDefaultInstr(), true);
        IToken token = getKeywordInstr();
        for (String func : KEYWORDS)
            wordRule.addWord(func, token);
        token = getFunctionInstr();
        for (String func : BUILTIN_FUN)
            wordRule.addWord(func, token);
        return wordRule;
    }

    private IToken getDefaultInstr() {
        IToken defaultInstr = new Token(new TextAttribute(
                manager.getColor(IPigLatinColorConstants.DEFAULT)));
        return defaultInstr;
    }

    private IToken getKeywordInstr() {
        IToken keywordInstr = new Token(new TextAttribute(
                manager.getColor(IPigLatinColorConstants.KEYWORD), null,
                SWT.BOLD));
        return keywordInstr;
    }

    private IToken getFunctionInstr() {
        IToken functionInstr = new Token(new TextAttribute(
                manager.getColor(IPigLatinColorConstants.BUILTIN_FUNCTION),
                null, SWT.ITALIC));
        return functionInstr;
    }
}
