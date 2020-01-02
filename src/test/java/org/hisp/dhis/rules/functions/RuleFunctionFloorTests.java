package org.hisp.dhis.rules.functions;

import org.hamcrest.MatcherAssert;
import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.antlr.ExpressionParser;
import org.hisp.dhis.parser.expression.function.ScalarFunction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;



@RunWith( MockitoJUnitRunner.class )
public class RuleFunctionFloorTests
{

        @Mock
        private ExpressionParser.ExprContext context;

        @Mock
        private CommonExpressionVisitor visitor;

        @Mock
        private ExpressionParser.ExprContext mockedFirstExpr;

        @Before
        public void setUp() {
                when(context.expr(0)).thenReturn( mockedFirstExpr );
        }

        @Test
        public void evaluateMustReturnFlooredValue()
        {
                RuleFunctionFloor floor = new RuleFunctionFloor();

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "4.1" );
                MatcherAssert.assertThat(floor.evaluate(context, visitor), is("4"));

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "0.8" );
                MatcherAssert.assertThat(floor.evaluate(context, visitor), is("0"));

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "5.1" );
                MatcherAssert.assertThat(floor.evaluate(context, visitor), is("5"));

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "1.0" );
                MatcherAssert.assertThat(floor.evaluate(context, visitor), is("1"));

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-9.3" );
                MatcherAssert.assertThat(floor.evaluate(context, visitor), is("-10"));

                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "-5.9" );
                MatcherAssert.assertThat(floor.evaluate(context, visitor), is("-6"));
        }

        @Test
        public void return_zero_when_number_is_invalid()
        {
                when( visitor.castStringVisit( mockedFirstExpr ) ).thenReturn( "not a number" );

                ScalarFunction floor = new RuleFunctionFloor();

                MatcherAssert.assertThat( floor.evaluate( context, visitor ), is("0") );
        }
}
