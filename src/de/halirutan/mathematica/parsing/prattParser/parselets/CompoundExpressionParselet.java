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
 * @author patrick (3/27/13)
 */
public class CompoundExpressionParselet implements InfixParselet {
    private final int m_precedence;

    public CompoundExpressionParselet(int precedence) {
        this.m_precedence = precedence;
    }

    @Override
    public int getPrecedence() {
        return m_precedence;
    }

    @Override
    public MathematicaParser.Result parse(MathematicaParser parser, MathematicaParser.Result left) throws CriticalParserError {
        if (!left.isValid()) return MathematicaParser.notParsed();
        PsiBuilder.Marker compoundExprMark = left.getMark().precede();
        IElementType token = MathematicaElementTypes.COMPOUND_EXPRESSION_EXPRESSION;
        parser.advanceLexer();

        boolean ok = true;

        while (true) {
            MathematicaParser.Result result = parser.parseExpression(m_precedence);
            if (!result.isValid()) break;
            ok &= result.isParsed();
            if (parser.matchesToken(MathematicaElementTypes.SEMICOLON)) parser.advanceLexer();
            else break;
        }

        compoundExprMark.done(token);
        return MathematicaParser.result(compoundExprMark, token, ok);

    }
}
