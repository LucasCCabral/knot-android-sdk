package com.cesar.knot_sdk.knot_state_machine.states.base_classes.waiting_state

sealed class WaitingStateActions {
    object OpOk : WaitingStateActions()
    object OpNotOk : WaitingStateActions()
    object Timeout : WaitingStateActions()
    object EAGAIN : WaitingStateActions()
    object ENOTCONN : WaitingStateActions()
    object UnregisterRequest : WaitingStateActions()
}
