package com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state

import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralStateActions.OpOk
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state.ProceduralStateActions.Timeout

abstract class ProceduralState : State() {

    abstract override fun enter()

    abstract override fun getErrorCode() : ProceduralStateActions

    abstract override fun getNextState(message : String) : State

    override fun getNextAction(message : String) : ProceduralStateActions {
        if(error != NO_ERROR_MESSAGE) return getErrorCode()
        if(message == TIMEOUT_MESSAGE) return Timeout
        return OpOk
    }

}
