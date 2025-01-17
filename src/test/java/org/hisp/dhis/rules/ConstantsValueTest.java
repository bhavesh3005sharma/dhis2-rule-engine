package org.hisp.dhis.rules;

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

import com.google.common.collect.Maps;
import org.hisp.dhis.rules.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith( JUnit4.class )
public class ConstantsValueTest
{

    @Test( expected = IllegalArgumentException.class )
    public void shouldThrowExceptionIfConstantsValueMapIsNull()
    {
        RuleEngineContext.builder()
            .rules( Arrays.asList( mock( org.hisp.dhis.rules.models.Rule.class ) ) )
            .ruleVariables( Arrays.asList( mock( RuleVariable.class ) ) )
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( null )
            .build();

    }

    @Test
    public void assignConstantValueFromAssignActionInEnrollment()
        throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create( null, "C{A1234567890}", "#{test_attribute}" );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", Arrays.asList( assignAction ), "test_program_rule1", "" );

        Map<String, String> constantsValueMap = new HashMap<>();
        constantsValueMap.put( "A1234567890", "3.14" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList( rule ), constantsValueMap );

        RuleEnrollment enrollment = RuleEnrollment.builder()
            .enrollment( "test_enrollment" )
            .programName( "test_program" )
            .incidentDate( new Date() )
            .enrollmentDate( new Date() )
            .status( RuleEnrollment.Status.ACTIVE )
            .organisationUnit( "test_ou" )
            .organisationUnitCode( "test_ou_code" )
            .attributeValues( Arrays.asList( RuleAttributeValue.create( "test_attribute", "test_value" ) ) )
            .build();

        RuleEngine ruleEngine = ruleEngineBuilder.build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( enrollment ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "3.14" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( assignAction );
    }

    @Test
    public void assignValue()
        throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create( null, "4", "test_attribute" );
        RuleAction action = RuleActionShowError.create( null, "#{test_attribute}", "" );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", Arrays.asList( assignAction ), "test_program_rule1", "" );
        org.hisp.dhis.rules.models.Rule rule2 = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "#{test_attribute} > 3", Arrays.asList( action ), "test_program_rule2", "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList( rule, rule2 ),
            Maps.<String, String>newHashMap() );

        RuleEnrollment enrollment = RuleEnrollment.builder()
            .enrollment( "test_enrollment" )
            .programName( "test_program" )
            .incidentDate( new Date() )
            .enrollmentDate( new Date() )
            .status( RuleEnrollment.Status.ACTIVE )
            .organisationUnit( "test_ou" )
            .organisationUnitCode( "test_ou_code" )
            .attributeValues( Arrays.asList( RuleAttributeValue.create( "test_attribute", "test_value" ) ) )
            .build();

        RuleEngine ruleEngine = ruleEngineBuilder.build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( enrollment ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 2 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( assignAction );
        assertThat( ruleEffects.get( 1 ).data() ).isEqualTo( "4" );
        assertThat( ruleEffects.get( 1 ).ruleAction() ).isEqualTo( action );
    }

    @Test
    public void assignValueThroughVariable()
        throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create( "#{test_attribute}", "4", null );
        RuleAction action = RuleActionShowError.create( null, "#{test_attribute}", "" );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", Arrays.asList( assignAction ), "test_program_rule1", "" );
        org.hisp.dhis.rules.models.Rule rule2 = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "#{test_attribute} > 3", Arrays.asList( action ), "test_program_rule2", "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList( rule, rule2 ),
            Maps.<String, String>newHashMap() );

        RuleEnrollment enrollment = RuleEnrollment.builder()
            .enrollment( "test_enrollment" )
            .programName( "test_program" )
            .incidentDate( new Date() )
            .enrollmentDate( new Date() )
            .status( RuleEnrollment.Status.ACTIVE )
            .organisationUnit( "test_ou" )
            .organisationUnitCode( "test_ou_code" )
            .attributeValues( Arrays.asList( RuleAttributeValue.create( "test_attribute", "test_value" ) ) )
            .build();

        RuleEngine ruleEngine = ruleEngineBuilder.build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( enrollment ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "4" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( action );
    }

    @Test
    public void assignConstantValueFromAssignActionInEvent()
        throws Exception
    {
        RuleAction assignAction = RuleActionAssign.create( null, "C{A1234567890}", "#{test_data_element}" );
        org.hisp.dhis.rules.models.Rule rule = org.hisp.dhis.rules.models.Rule
            .create( null, 1, "true", Arrays.asList( assignAction ), "test_program_rule1", "" );

        Map<String, String> constantsValueMap = new HashMap<>();
        constantsValueMap.put( "A1234567890", "3.14" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngine( Arrays.asList( rule ), constantsValueMap );

        RuleEvent ruleEvent = RuleEvent.builder()
            .event( "test_event" )
            .programStage( "test_program_stage" )
            .programStageName( "" )
            .status( RuleEvent.Status.ACTIVE )
            .eventDate( new Date() )
            .dueDate( new Date() )
            .organisationUnit( "" )
            .organisationUnitCode( "" )
            .completedDate( new Date() )
            .dataValues( Arrays.asList( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ) )
            .build();

        RuleEngine ruleEngine = ruleEngineBuilder.build();
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "3.14" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( assignAction );

    }

    private RuleEngine.Builder getRuleEngine( List<Rule> rules,
        Map<String, String> constantsValueMap )
    {
        return RuleEngineContext
            .builder()
            .rules( rules )
            .ruleVariables( Arrays.<RuleVariable>asList() )
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( constantsValueMap )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }

}
