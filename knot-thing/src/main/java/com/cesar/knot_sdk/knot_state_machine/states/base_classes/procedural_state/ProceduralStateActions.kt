package com.cesar.knot_sdk.knot_state_machine.states.base_classes.procedural_state

sealed class ProceduralStateActions {
    object OpOk : ProceduralStateActions()
    object OpNotOk : ProceduralStateActions()
    object Timeout : ProceduralStateActions()
    object EAGAIN : ProceduralStateActions()
    object ENOTCONN : ProceduralStateActions()
    object UnregisterRequest : ProceduralStateActions()
}
