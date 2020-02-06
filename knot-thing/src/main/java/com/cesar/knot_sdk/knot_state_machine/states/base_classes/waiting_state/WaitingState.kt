package com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state

import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state.WaitingStateActions.Timeout

abstract class WaitingState : State() {

    abstract override fun enter()

    /**
     * This method consumes and processes a json message.
     */
    abstract fun consumeMessage(message : String) : WaitingStateActions

    abstract override fun getErrorCode() : WaitingStateActions

    abstract override fun getNextState(message : String) : State

    override fun getNextAction(message : String) : WaitingStateActions {
        if(error != NO_ERROR_MESSAGE) return getErrorCode()
        if(message == TIMEOUT_MESSAGE) return Timeout
        else return consumeMessage(message)
    }

}
