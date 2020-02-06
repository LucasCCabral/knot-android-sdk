package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_messages.KNoTMessageAuth
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.PREF_MISSING
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.TOKEN_PREF
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.UUID_PREF
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessageParser
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.sharedPref
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingStateActions
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingStateActions.*
import java.io.IOException

class Authenticating : WaitingState() {

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

    override fun consumeMessage(message : String) : WaitingStateActions {
        val res = knotMessageParser.parseAuthStatus(message)
        if(res.error != null) return OpNotOk
        return OpOk
    }

    override fun getErrorCode() : WaitingStateActions {
        return OpNotOk
    }

   override fun getNextState(message : String) : State {
        val action = getNextAction(message)

        return when(action) {
            OpOk              -> TODO("This transition should lead to the schema state which does not exist yet")
            OpNotOk           -> TODO("This transition should lead to the error state, which does not exist yet")
            Timeout           -> this
            EAGAIN            -> this
            ENOTCONN          -> Disconnected()
            UnregisterRequest -> TODO("This transition should lead to the unregister state, which does not exist yet")
        }
    }
}
