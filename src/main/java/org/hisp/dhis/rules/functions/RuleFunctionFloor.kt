package org.hisp.dhis.rules.functions

import org.hisp.dhis.rules.RuleVariableValue
import kotlin.math.floor

class RuleFunctionFloor : RuleFunction() {

    override fun evaluate(arguments: List<String>,
                          valueMap: Map<String, RuleVariableValue>, supplementaryData: Map<String, List<String>>?): String {
        when {
            arguments.size != 1 -> throw IllegalArgumentException("One argument was expected, ${arguments.size} were supplied")
            else -> return floor(toDouble(arguments[0], 0.0)).toLong().toString()
        }
    }

    companion object {
        const val D2_FLOOR = "d2:floor"

        @JvmStatic
        fun create() = RuleFunctionFloor()
    }
}
