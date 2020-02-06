package com.cesar.knot_sdk.knot_state_machine.states.base_classes

abstract class State {

    companion object {
        const val NO_ERROR_MESSAGE : String = "no error"
        const val NETWORK_ERROR_MESSAGE : String = "network error"
        const val TIMEOUT_MESSAGE  : String = "timeout"
        const val PROCEED_MESSAGE : String = "proceed"
    }

    var error : String = NO_ERROR_MESSAGE

    /**
     * This method describes a set of actions that should be performed the first
     * time a state is accessed.
     */
    abstract fun enter()

    /**
     * This method checks for any Errors and returs the corresponding action
     */
    abstract fun getErrorCode() : Any

    /**
     * This method describes the set of actions that should be performed when
     * the leaving condition has been met.
     */
    abstract fun getNextState(message : String = PROCEED_MESSAGE) : State

    /**
     * This method is supposed to get the next action for the getNextState
     * method. This is a private method.
     */
    abstract fun getNextAction(message : String) : Any

}
