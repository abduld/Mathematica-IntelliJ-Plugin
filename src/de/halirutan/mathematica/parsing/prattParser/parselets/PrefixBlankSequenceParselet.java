/*
 * Mathematica Plugin for Jetbrains IDEA
 * Copyright (C) 2013 Patrick Scheibe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.halirutan.mathematica.parsing.prattParser.parselets;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import de.halirutan.mathematica.parsing.MathematicaElementTypes;
import de.halirutan.mathematica.parsing.prattParser.CriticalParserError;
import de.halirutan.mathematica.parsing.prattParser.MathematicaParser;

/**
 * Parses __expr. Note expr must not be present.
 *
 * @author patrick (3/27/13)
 */
public class PrefixBlankSequenceParselet implements PrefixParselet {
    private final int m_precedence;

    public PrefixBlankSequenceParselet(int precedence) {
        this.m_precedence = precedence;
    }

    @Override
    public MathematicaParser.Result parse(MathematicaParser parser) throws CriticalParserError {
        PsiBuilder.Marker blankMark = parser.mark();
        IElementType token = MathematicaElementTypes.BLANK_SEQUENCE_EXPRESSION;
        parser.advanceLexer();
        MathematicaParser.Result result = parser.parseExpression(m_precedence);
        blankMark.done(token);
        return MathematicaParser.result(blankMark, token, !result.isValid() || result.isParsed());
    }
}
