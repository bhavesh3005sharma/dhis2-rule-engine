package org.hisp.dhis.rules;

import org.hisp.dhis.rules.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( JUnit4.class )
public class RuleEngineVariableNameTests
{
    private final static String UID0 = "Aabcde12345";
    private final static String UID0WILD = "Babcde12345.*";
    private final static String UID01 = "Cabcde12345.Dabcde12345";
    private final static String UID01WILD = "Eabcde12345.Fabcde12345.*";
    private final static String UID012 = "Gabcde12345.Habcde12345.Iabcde12345";
    private final static String UID0WILD2 = "Labcde12345.*.Mabcde12345";

    private final static String VARIABLE_NAME = "Variable.name_3_4-1";

    @Test
    public void evaluateD2Round()
        throws Exception
    {
        RuleAction ruleAction1 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{" + UID01 + "})" );
        RuleAction ruleAction2 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{" + VARIABLE_NAME + "})" );
        RuleAction ruleAction3 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{" + UID0 + "})" );
        RuleAction ruleAction4 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{" + UID0WILD + "})" );
        RuleAction ruleAction5 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{" + UID01WILD + "})" );
        RuleAction ruleAction6 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{" + UID012 + "})" );
        RuleAction ruleAction7 = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:round(#{" + UID0WILD2 + "})" );
        RuleVariable ruleVariable1 = RuleVariableNewestEvent.create(
            UID01, "test_data_element1", RuleValueType.NUMERIC );
        RuleVariable ruleVariable2 = RuleVariableNewestEvent.create(
            VARIABLE_NAME, "test_data_element2", RuleValueType.NUMERIC );
        RuleVariable ruleVariable3 = RuleVariableNewestEvent.create(
            UID0, "test_data_element3", RuleValueType.NUMERIC );
        RuleVariable ruleVariable4 = RuleVariableNewestEvent.create(
            UID0WILD, "test_data_element4", RuleValueType.NUMERIC );
        RuleVariable ruleVariable5 = RuleVariableNewestEvent.create(
            UID01WILD, "test_data_element5", RuleValueType.NUMERIC );
        RuleVariable ruleVariable6 = RuleVariableNewestEvent.create(
            UID012, "test_data_element6", RuleValueType.NUMERIC );
        RuleVariable ruleVariable7 = RuleVariableNewestEvent.create(
            UID0WILD2, "test_data_element7", RuleValueType.NUMERIC );

        List<RuleAction> actions = Arrays
            .asList( ruleAction1, ruleAction2, ruleAction3, ruleAction4, ruleAction5, ruleAction6, ruleAction7 );
        Rule rule = Rule.create( null, null, "true",
            actions, "");

        List<RuleVariable> ruleVariables = Arrays
            .asList( ruleVariable1, ruleVariable2, ruleVariable3, ruleVariable4, ruleVariable5, ruleVariable6, ruleVariable7 );
        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            ruleVariables );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "",null,Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element1", "2.6" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element2", "2.6" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element3", "2.6" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element4", "2.6" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element5", "2.6" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element6", "2.6" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element7", "2.6" )
                ), "");

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 7 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction1 );
        assertEquals( "3", ruleEffects.get( 0 ).data() );
        assertThat( ruleEffects.get( 1 ).ruleAction() ).isEqualTo( ruleAction2 );
        assertEquals( "3", ruleEffects.get( 1 ).data() );
        assertThat( ruleEffects.get( 2 ).ruleAction() ).isEqualTo( ruleAction3 );
        assertEquals( "3", ruleEffects.get( 2 ).data() );
        assertThat( ruleEffects.get( 3 ).ruleAction() ).isEqualTo( ruleAction4 );
        assertEquals( "3", ruleEffects.get( 3 ).data() );
        assertThat( ruleEffects.get( 4 ).ruleAction()).isEqualTo( ruleAction5 );
        assertEquals( "3", ruleEffects.get( 4 ).data() );
        assertThat( ruleEffects.get( 5 ).ruleAction() ).isEqualTo( ruleAction6 );
        assertEquals( "3", ruleEffects.get( 5 ).data() );
        assertThat( ruleEffects.get( 6 ).ruleAction() ).isEqualTo( ruleAction7 );
        assertEquals( "3", ruleEffects.get( 6 ).data() );
    }

    @Test
    public void evaluateHasValueFunctionMustReturnTrueIfVariableIsComposedUIDs()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:hasValue(#{" + UID01 + "})" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent.create(
            UID01, "test_data_element", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ), "" );
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "true" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateHasValueFunctionMustReturnTrueIfVariableIsVariableName()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:hasValue(#{" + VARIABLE_NAME + "})" );
        RuleVariable ruleVariable = RuleVariableCurrentEvent.create(
            VARIABLE_NAME, "test_data_element", RuleValueType.TEXT );
        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine ruleEngine = getRuleEngine( rule, Arrays.asList( ruleVariable ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList( RuleDataValue.create(
                new Date(), "test_program_stage", "test_data_element", "test_value" ) ), "" );
        List<RuleEffect> ruleEffects = ruleEngine.evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "true" );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2CountIfValueIsVariableName()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:countIfValue(#{"+VARIABLE_NAME+"}, 'condition')" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );
        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition2" ) ), "" );
        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );

        ruleEngineBuilder.events( Arrays.asList( ruleEvent2, ruleEvent3 ) );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertTrue( ruleEffects.get( 0 ).data().equals( "2" ) );
    }

    @Test
    public void evaluateD2CountIfValueIsComposedUid()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:countIfValue(#{"+UID01+"}, 'condition')" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            UID01, "test_data_element_one", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );
        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition2" ) ), "" );
        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );

        ruleEngineBuilder.events( Arrays.asList( ruleEvent2, ruleEvent3 ) );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertTrue( ruleEffects.get( 0 ).data().equals( "2" ) );
    }

    @Test
    public void evaluateD2CountIfVariableName()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:count(#{"+VARIABLE_NAME+"})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );
        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition2" ) ), "" );
        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );

        RuleEvent ruleEvent4 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "condition" ) ), "" );

        ruleEngineBuilder.events( Arrays.asList( ruleEvent2, ruleEvent3, ruleEvent4 ) );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "3", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2CountIfVariableNameIfComposedUid()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:count(#{"+UID01+"})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            UID01, "test_data_element_one", RuleValueType.TEXT );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );
        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition2" ) ), "" );
        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "condition" ) ), "" );

        RuleEvent ruleEvent4 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "condition" ) ), "" );

        ruleEngineBuilder.events( Arrays.asList( ruleEvent2, ruleEvent3, ruleEvent4 ) );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.build().evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "3", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2CountIfZeroPosIfVariableName()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos(#{"+VARIABLE_NAME+"})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.NUMERIC );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "0" ) ), "" );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "1" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "-3" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "2", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2CountIfZeroPosIfComposedUid()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayText.createForFeedback(
            "test_action_content", "d2:countIfZeroPos(#{"+UID01+"})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            UID01, "test_data_element_one", RuleValueType.NUMERIC );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule, Arrays.asList( ruleVariableOne ) );

        RuleEvent ruleEvent = RuleEvent.create( "test_event", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "0" ) ), "" );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "1" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event1", "test_program_stage",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "-3" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertEquals( "2", ruleEffects.get( 0 ).data() );
    }

    @Test
    public void evaluateD2MaxValueIfVariableName()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "true" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.NUMERIC );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "d2:maxValue(#{"+VARIABLE_NAME+"}) == 8.0", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "7" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "8" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent3 ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void evaluateD2MaxValueIfComposedUid()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "true" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            UID01, "test_data_element_one", RuleValueType.NUMERIC );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "d2:maxValue(#{"+UID01+"}) == 8.0", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "7" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "8" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent3 ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
    }

    @Test
    public void testMinValueIfVariableName()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:minValue(#{"+VARIABLE_NAME+"})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            VARIABLE_NAME, "test_data_element_one", RuleValueType.NUMERIC );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "7" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "8" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent3 ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "5.0" );
    }

    @Test
    public void testMinValueIfComposedUid()
        throws Exception
    {
        RuleAction ruleAction = RuleActionDisplayKeyValuePair.createForFeedback(
            "test_action_content", "d2:minValue(#{"+UID01+"})" );
        RuleVariable ruleVariableOne = RuleVariableNewestEvent.create(
            UID01, "test_data_element_one", RuleValueType.NUMERIC );

        RuleVariable ruleVariableTwo = RuleVariableNewestEvent.create(
            "test_var_two", "test_data_element_two", RuleValueType.TEXT );

        Rule rule = Rule.create( null, null, "true", Arrays.asList( ruleAction ), "" );

        RuleEngine.Builder ruleEngineBuilder = getRuleEngineBuilder( rule,
            Arrays.asList( ruleVariableOne, ruleVariableTwo ) );

        RuleEvent ruleEvent1 = RuleEvent.create( "test_event1", "test_program_stage1",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "5" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent2 = RuleEvent.create( "test_event2", "test_program_stage2",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "7" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        RuleEvent ruleEvent3 = RuleEvent.create( "test_event3", "test_program_stage3",
            RuleEvent.Status.ACTIVE, new Date(), new Date(), "", null, Arrays.asList(
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_one", "8" ),
                RuleDataValue.create( new Date(), "test_program_stage", "test_data_element_two", "male" ) ), "" );

        List<RuleEffect> ruleEffects = ruleEngineBuilder.events( Arrays.asList( ruleEvent1, ruleEvent2 ) ).build()
            .evaluate( ruleEvent3 ).call();

        assertThat( ruleEffects.size() ).isEqualTo( 1 );
        assertThat( ruleEffects.get( 0 ).ruleAction() ).isEqualTo( ruleAction );
        assertThat( ruleEffects.get( 0 ).data() ).isEqualTo( "5.0" );
    }

    private RuleEngine getRuleEngine( Rule rule, List<RuleVariable> ruleVariables )
    {
        return getRuleEngineBuilder( rule, ruleVariables )
            .build();
    }

    private RuleEngine.Builder getRuleEngineBuilder( Rule rule, List<RuleVariable> ruleVariables )
    {
        return RuleEngineContext
            .builder()
            .rules( Arrays.asList( rule ) )
            .ruleVariables( ruleVariables )
            .calculatedValueMap( new HashMap<String, Map<String, String>>() )
            .supplementaryData( new HashMap<String, List<String>>() )
            .constantsValue( new HashMap<String, String>() )
            .build().toEngineBuilder().triggerEnvironment( TriggerEnvironment.SERVER );
    }
}
