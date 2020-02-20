package com.cesar.knot_sdk.knot_state_machine.states.base_classes

import com.cesar.knot_sdk.knot_messages.KNoTMessageAuthenticated
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_messages.KNoTMessageRemoved
import com.cesar.knot_sdk.knot_messages.KNoTMessageSchemaResp

sealed class KNoTEvent {
    object NotReady                                        : KNoTEvent()
    object Ready                                           : KNoTEvent()
    object Timeout                                         : KNoTEvent()
    class RegOK(val res : KNoTMessageRegistered)           : KNoTEvent()
    class RegNotOk(val res : KNoTMessageRegistered)        : KNoTEvent()
    class AuthNotOk(val res : KNoTMessageAuthenticated)    : KNoTEvent()
    class AuthOk(val res : KNoTMessageAuthenticated)       : KNoTEvent()
    class SchemaOk(val res : KNoTMessageSchemaResp)        : KNoTEvent()
    class SchemaNotOk(val res : KNoTMessageSchemaResp)     : KNoTEvent()
    class UnregisterEvent(val res : KNoTMessageRemoved)    : KNoTEvent()
}
