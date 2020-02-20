package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_messages.KNoTMessageRequestData
import com.cesar.knot_sdk.knot_messages.KNoTMessageUpdateData
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.getUUID
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotDataManager
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import java.io.IOException

class Online : State() {

    override fun enter() {
        val thingId = getUUID()
        val updateData = KNoTMessageUpdateData(
            thingId!!,
            knotDataManager.getAllKNoTDataValues()
        )

        try {
            knotMessager.publishData(updateData)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
    }

   fun getData(requestData : KNoTMessageRequestData) : State {
       val requestedSensorValues = KNoTMessageUpdateData(
           requestData.id,
           knotDataManager.getKNoTDataValues(requestData.sensorIds)
       )

       knotMessager.publishData(requestedSensorValues)
       return this
   }

    override fun getNextState(event : KNoTEvent) = when(event) {
        is NotReady        -> Disconnected()
        is Ready           -> this
        is Timeout         -> this
        is RegOK           -> this
        is RegNotOk        -> this
        is AuthNotOk       -> this
        is AuthOk          -> this
        is SchemaOk        -> this
        is SchemaNotOk     -> this
        is UnregisterEvent -> Unregister()
        is DataRequest     -> getData(event.res)
    }

}
