package org.hisp.dhis.parser.expression.function;

/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;

import static org.hisp.dhis.parser.expression.CommonExpressionVisitor.DEFAULT_DOUBLE_VALUE;
import static org.hisp.dhis.parser.expression.ParserUtils.castDate;
import static org.hisp.dhis.parser.expression.ParserUtils.castString;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.CompareDateContext;
import static org.hisp.dhis.parser.expression.antlr.ExpressionParser.ExprContext;

/**
 * Program indicator date/time between functions
 *
 * @author Jim Grace
 */
public abstract class ProgramBetweenFunction
    extends SimpleScalarFunction
{
    @Override
    public final Object evaluate( ExprContext ctx, CommonExpressionVisitor visitor )
    {
        validateDateArg( ctx.compareDate( 0 ), visitor );
        validateDateArg( ctx.compareDate( 1 ), visitor );

        return DEFAULT_DOUBLE_VALUE;
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Validates a compare date
     *
     * @param ctx program function compare date context
     * @param visitor the program indicator expression tree visitor
     */
    private void validateDateArg( CompareDateContext ctx, CommonExpressionVisitor visitor )
    {
        castDate( visitor.visit( ctx.expr() ) );
    }
}