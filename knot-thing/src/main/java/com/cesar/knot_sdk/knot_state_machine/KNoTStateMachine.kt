package com.cesar.knot_sdk.knot_state_machine

import android.content.Context
import com.cesar.knot_sdk.KNoTAMQP
import com.cesar.knot_sdk.KNoTDataManager
import com.cesar.knot_sdk.KNoTMessageParser
import com.cesar.knot_sdk.KNoTMessager
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State.Companion.PROCEED_MESSAGE
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.branching_state.BranchingState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingState

object KNoTStateMachine {

    const val DEFAULT_RABBIT_PORT_NUMBER = 5672
    const val HOSTNAME = ""
    const val PORT_NUMBER = DEFAULT_RABBIT_PORT_NUMBER
    const val USERNAME = ""
    const val PASSWORD = ""

    val knotAMQP = KNoTAMQP(USERNAME, PASSWORD, HOSTNAME, PORT_NUMBER)
    var state : State = Disconnected()
    lateinit var knotMessager : KNoTMessager
    lateinit var knotDataManager : KNoTDataManager
    lateinit var knotMessageParser : KNoTMessageParser
    lateinit var context : Context
    lateinit var thingName : String

    val PREF_ID = "preference file key"
    val UUID_PREF = "uuid"
    val TOKEN_PREF = "token"
    val SCHEMA_SENT = "schema sent"
    val PREF_MISSING = ""

    val sharedPref = context.getSharedPreferences(
        PREF_ID,
        Context.MODE_PRIVATE
    )

    fun operationResult(message : String = PROCEED_MESSAGE) {
        state = state.getNextState(message)
    }

    private fun waitForResponse() {
        TODO("This method will create a timer that checks for a timeout for the request")
    }

    fun enterState() {
        state.enter()

        when(state) {
            is ProceduralState -> operationResult()
            is WaitingState    -> waitForResponse()
            is BranchingState  -> operationResult()
        }
    }

    fun getUUID() = sharedPref.getString(UUID_PREF, PREF_MISSING)

}
