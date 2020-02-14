package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.LogWrapper
import com.cesar.knot_sdk.knot_messages.KNoTMessageAuth
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.PREF_MISSING
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.TOKEN_PREF
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.UUID_PREF
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.schemaChanged
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.sharedPref
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import java.io.IOException

class Authenticating : State() {

    override fun enter() {
        val uuid = sharedPref.getString(
            UUID_PREF,
            PREF_MISSING
        )
        val token = sharedPref.getString(
            TOKEN_PREF,
            PREF_MISSING
        )

        val knotMessageAuth = KNoTMessageAuth(uuid!!, token!!)

        try {
            knotMessager.authenticate(knotMessageAuth)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
    }

    private fun getCorrectNextState() : State {
        if (schemaChanged()) {
            LogWrapper.log("State: Auth, Error: $error")
            return Schema()
        }

        return TODO("Return online state once it's been created")
    }

    override fun getNextState(event : KNoTEvent) = when(event) {
        is NotReady         -> Disconnected()
        is Ready            -> this
        is Timeout          -> this
        is RegOK            -> this
        is RegNotOk         -> this
        is SchemaOk         -> this
        is SchemaNotOk      -> this
        is AuthOk           -> getCorrectNextState()
        is AuthNotOk        -> Unregister()
        is UnregisterEvent  -> Unregister()
    }

}
