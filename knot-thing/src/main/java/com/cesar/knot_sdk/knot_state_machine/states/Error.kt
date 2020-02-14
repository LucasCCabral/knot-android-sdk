package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.LogWrapper
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State

class Error : State() {

    override fun enter() = LogWrapper.log("Entered error mode")

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
    }

}
