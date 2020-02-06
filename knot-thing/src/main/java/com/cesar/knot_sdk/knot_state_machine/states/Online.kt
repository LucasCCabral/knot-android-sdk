package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_messages.KNoTMessageUpdateData
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.getUUID
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotDataManager
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralStateActions
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralStateActions.*
import java.io.IOException

class Online : ProceduralState() {

    override fun enter() {
        val thingId = getUUID()
        val updateData = KNoTMessageUpdateData(
            thingId!!,
            knotDataManager.getKNoTDataValues()
        )

        try {
            knotMessager.publishData(updateData)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }

    }

    override fun getErrorCode() : ProceduralStateActions {
        return OpNotOk
    }

    override fun getNextState(message : String) : State {
        val action = getNextAction(message)

        return when (action) {
            OpOk              -> TODO("This transition should lead to the runnning state, which does not exist yet")
            OpNotOk           -> Online()
            Timeout           -> Online()
            EAGAIN            -> Online()
            ENOTCONN          -> Disconnected()
            UnregisterRequest -> TODO("This transition should lead to the unregister state, which does not exist yet")
        }
    }
}
