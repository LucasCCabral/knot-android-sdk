package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_messages.KNoTMessageRegister
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.UUID_PREF
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.sharedPref
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.thingName
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import java.io.IOException
import java.util.*

class Registering : State() {

    override fun enter() {
        val thingId = UUID.randomUUID().toString()
        val knotMessageRegister = KNoTMessageRegister(thingId, thingName)
        try {
            knotMessager.register(knotMessageRegister)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
    }

    private fun consumeMessage(res : KNoTMessageRegistered) : State {
        KNoTStateMachine.storeCredentials(res)
        return TODO("Return schema state once it's been created")
    }

    override fun getNextState(event : KNoTEvent) = when(event) {
        is NotReady        -> Disconnected()
        is Ready           -> this
        is Timeout         -> this
        is RegOK           -> consumeMessage(event.res)
        is RegNotOk        -> this
        is AuthNotOk       -> this
        is AuthOk          -> this
        is SchemaOk        -> this
        is SchemaNotOk     -> this
        is UnregisterEvent -> this
    }

}
