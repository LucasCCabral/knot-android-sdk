package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_messages.KNoTMessageUpdateSchema
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.getUUID
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotDataManager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessageParser
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingStateActions
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingStateActions.*
import java.io.IOException

class Schema : WaitingState() {

    override fun enter() {
        val thingId = getUUID()
        val updateSchema = KNoTMessageUpdateSchema(
            thingId!!,
            knotDataManager.getKNoTDataSchemas()
        )

        try {
            knotMessager.updateSchema(updateSchema)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }

    }

    override fun consumeMessage(message : String) : WaitingStateActions {
        val message = knotMessageParser.parseSchemaStatus(message)

        if(message.error != null) return OpNotOk

        return OpOk
    }

    override fun getErrorCode() : WaitingStateActions {
        return OpNotOk
    }

    override fun getNextState(message : String) : State {
        val action = consumeMessage(message)

        return when(action) {
            OpOk              -> TODO("This transition should lead to the online state, which does not exist yet")
            OpNotOk           -> TODO("This transition should lead to the error state, which does not exist yet")
            Timeout           -> this
            EAGAIN            -> this
            ENOTCONN          -> Disconnected()
            UnregisterRequest -> TODO("This transition should lead to the unregister state, which does not exist yet")
        }
    }
}
