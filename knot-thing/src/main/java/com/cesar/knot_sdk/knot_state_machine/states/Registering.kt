package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_messages.KNoTMessageRegister
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.UUID_PREF
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessageParser
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.sharedPref
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.thingName
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingStateActions
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingStateActions.*
import java.io.IOException
import java.util.*

class Registering : WaitingState() {

    override fun enter() {
        val thingId = UUID.randomUUID().toString()
        val knotMessageRegister = KNoTMessageRegister(thingId, thingName)
        try {
            knotMessager.register(knotMessageRegister)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
    }

    override fun consumeMessage(message : String) : WaitingStateActions {
        val res = knotMessageParser.parseDeviceRegistered(message)

        if(res.error != null) return OpNotOk

        sharedPref.edit().putString(UUID_PREF, res.id).apply()
        sharedPref.edit().putString(UUID_PREF, res.token).apply()
        return OpOk
    }

    override fun getErrorCode() : WaitingStateActions {
        return OpNotOk
    }

    override fun getNextState(message : String) : State {
        val action = consumeMessage(message)

        return when (action) {
            OpOk              -> TODO("This transition should lead to the schema state which does not exist yet")
            OpNotOk           -> TODO("This transition should lead to the error state, which does not exist yet")
            Timeout           -> this
            ENOTCONN          -> Disconnected()
            EAGAIN            -> this
            UnregisterRequest -> TODO("This transition should lead to the unregister state, which does not exist yet")

        }
    }
}
