package org.hisp.dhis.rules.functions;

/*
 * Copyright (c) 2004-2018, University of Oslo
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

import org.hamcrest.CoreMatchers;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.rules.parser.expression.CommonExpressionVisitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionLengthTest
{
    @Mock
    private ExpressionParser.ExprContext context;

    @Mock
    private CommonExpressionVisitor visitor;

    @Mock
    private ExpressionParser.ExprContext mockedFirstExpr;

    @Before
    public void setUp()
    {
        when( context.expr( 0 ) ).thenReturn( mockedFirstExpr );
    }

    @Test
    public void return_length_of_argument()
    {
        RuleFunctionLength lengthFunction = new RuleFunctionLength();
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "" );

        assertThat( lengthFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "0" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abc" );
        assertThat( lengthFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "3" ) );

        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "abcdef" );
        assertThat( lengthFunction.evaluate( context, visitor ), CoreMatchers.<Object>is( "6" ) );
    }

    @Test( expected = NullPointerException.class )
    public void throw_null_pointer_exception_when_arguments_is_null()
    {
        when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( null );
        new RuleFunctionLength().evaluate( context, visitor );
    }
}
