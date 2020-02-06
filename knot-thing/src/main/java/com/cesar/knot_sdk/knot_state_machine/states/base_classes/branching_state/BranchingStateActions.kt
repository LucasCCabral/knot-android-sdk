package com.cesar.knot_sdk.knot_state_machine.states.base_classes.branching_state

sealed class BranchingStateActions  {
     object TaskDone : BranchingStateActions()
     object TaskNotDone  : BranchingStateActions()
     object Timeout : BranchingStateActions()
     object EAGAIN : BranchingStateActions()
     object ENOTCONN : BranchingStateActions()
     object Error : BranchingStateActions()
     object UnregisterRequest : BranchingStateActions()
}
