package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_messages.KNoTMessageUpdateSchema
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.getUUID
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotDataManager
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import java.io.IOException

class Schema : State() {

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

    override fun getNextState(event : KNoTEvent) = when(event) {
        is NotReady        -> Disconnected()
        is Ready           -> this
        is Timeout         -> this
        is RegOK           -> this
        is RegNotOk        -> this
        is AuthNotOk       -> this
        is AuthOk          -> this
        is SchemaOk        -> Online()
        is SchemaNotOk     -> Error()
        is UnregisterEvent -> Unregister()
        is DataRequest     -> this
    }

}
