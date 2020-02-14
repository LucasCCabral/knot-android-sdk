package com.cesar.knot_sdk.knot_state_machine.states

import java.io.IOException
import com.cesar.knot_sdk.KNoTAMQPController
import com.cesar.knot_sdk.KNoTAMQPFactory
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotAMQP
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State.ReadyStates.*

class Disconnected : State() {

    enum class PossibleNextStates {
        REGISTER,
        AUTHENTICATE
    }

    override fun enter() {
        try {
            KNoTAMQPFactory().create(knotAMQP)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
        knotMessager = KNoTAMQPController(knotAMQP)
    }

    private fun getCorrectNextState() : PossibleNextStates {
        val possibleStates = KNoTStateMachine.getUUUID()

        if(possibleStates == PREF_MISSING) return REGISTER
        return AUTHENTICATE
    }

    private fun getBranchState() : Nothing {
        val nextState = getCorrectNextState()

        when(nextState) {
            REGISTER     -> TODO("Return register state once it's been created")
            AUTHENTICATE -> TODO("Return register state once it's been created")
        }
    }

    override fun getNextState(event : KNoTEvent) = when(event) {
        is NotReady        -> this
        is Ready           -> getBranchState()
        is Timeout         -> this
        is RegOK           -> this
        is RegNotOk        -> this
        is AuthNotOk       -> this
        is AuthOk          -> this
        is SchemaOk        -> this
        is SchemaNotOk     -> this
        is UnregisterEvent -> this
    }

}

