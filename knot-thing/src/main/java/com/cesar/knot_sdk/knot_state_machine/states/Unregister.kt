package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State

class Unregister : State() {

    override fun enter() {
        KNoTStateMachine.clearCredentials()
    }

    override fun getNextState(event : KNoTEvent) = when(event) {
        is NotReady        -> this
        is Ready           -> this
        is Timeout         -> this
        is RegOK           -> this
        is RegNotOk        -> this
        is AuthNotOk       -> this
        is AuthOk          -> this
        is SchemaOk        -> this
        is SchemaNotOk     -> this
        is UnregisterEvent -> this
        is DataRequest     -> this
        is DataUpdate      -> this
    }

}
