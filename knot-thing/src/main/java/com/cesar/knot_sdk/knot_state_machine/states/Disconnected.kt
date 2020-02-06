package com.cesar.knot_sdk.knot_state_machine.states

import java.io.IOException
import com.cesar.knot_sdk.KNoTAMQPController
import com.cesar.knot_sdk.KNoTAMQPFactory
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotAMQP
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralStateActions
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralStateActions.*

class Disconnected : ProceduralState() {

    override fun enter() {
        try {
            KNoTAMQPFactory().create(knotAMQP)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
        knotMessager = KNoTAMQPController(knotAMQP)
    }

    override fun getErrorCode() : ProceduralStateActions {
        if(error == NETWORK_ERROR_MESSAGE) return EAGAIN
        return ENOTCONN
    }

    override fun getNextState(message : String) : State {
        val action = getNextAction(message)

        return when (action) {
            OpOk              -> TODO("This transition should lead to the connected state, which does not exist yet")
            OpNotOk           -> this
            Timeout           -> this
            EAGAIN            -> this
            ENOTCONN          -> this
            UnregisterRequest -> this
        }
    }

}

