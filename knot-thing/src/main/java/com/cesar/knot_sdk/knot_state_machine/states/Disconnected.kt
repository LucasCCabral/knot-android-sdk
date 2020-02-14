package com.cesar.knot_sdk.knot_state_machine.states

import java.io.IOException
import com.cesar.knot_sdk.KNoTAMQPController
import com.cesar.knot_sdk.KNoTAMQPFactory
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.PREF_MISSING
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotAMQP
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State

class Disconnected : State() {

    override fun enter() {
        try {
            KNoTAMQPFactory().create(knotAMQP)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
        knotMessager = KNoTAMQPController(knotAMQP)
    }

    private fun getCorrectNextState() : State {
        val possibleStates = KNoTStateMachine.getUUID()

        if(possibleStates == PREF_MISSING) return Registering()
        return Authenticating()
    }

    override fun getNextState(event : KNoTEvent) : State = when(event) {
        is NotReady        -> this
        is Ready           -> getCorrectNextState()
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
