package com.cesar.knot_sdk.knot_state_machine.states

import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.PREF_MISSING
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.UUID_PREF
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.sharedPref
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.branching_state.BranchingState
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.branching_state.BranchingStateActions.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.branching_state.BranchingStateActions

class Connected : BranchingState() {

    val AUTHENTICATION_STATE = "authenticate"
    val REGISTRATION_STATE = "register"
    var operation = AUTHENTICATION_STATE

    override fun enter() {
        val uuid = sharedPref.getString(
            UUID_PREF,
            PREF_MISSING
        )
        if (uuid == PREF_MISSING) operation = REGISTRATION_STATE
    }

    override fun getErrorCode() : BranchingStateActions {
        return TaskDone
    }

    override fun getBranchingAction() = when(operation) {
            AUTHENTICATION_STATE -> TaskDone
            REGISTRATION_STATE   -> TaskNotDone
            else                 -> Error
    }

    override fun getNextState(message : String) : State {
        val action = getNextAction(message)

        return when(action) {
            TaskDone          -> Authenticating()
            TaskNotDone       -> Registering()
            Timeout           -> TODO("This transition should lead to the error state, which does not exist yet")
            EAGAIN            -> TODO("This transition should lead to the error state, which does not exist yet")
            ENOTCONN          -> TODO("This transition should lead to the error state, which does not exist yet")
            UnregisterRequest -> TODO("This transition should lead to the error state, which does not exist yet")
            Error             -> TODO("This transition should lead to the error state, which does not exist yet")
        }
    }

}
