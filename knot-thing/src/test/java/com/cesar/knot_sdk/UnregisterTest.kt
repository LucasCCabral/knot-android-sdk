package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_messages.KNoTMessageAuthenticated
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_messages.KNoTMessageRemoved
import com.cesar.knot_sdk.knot_messages.KNoTMessageSchemaResp
import com.cesar.knot_sdk.knot_state_machine.states.Unregister
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import org.junit.Test

class UnregisterTest {

    private val randomThingId = "0123456789abcde"
    private val nullError = "null"
    private val error = "non null error"
    private val randomToken = "random token"
    private val state = Unregister()

    @Test
    fun timeoutEvent_isUnregister() {
        val timeoutEvent = Timeout
        val nextState = state.getNextState(timeoutEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun notReadyEvent_isUnregister() {
        val notReadyEvent = NotReady
        val nextState = state.getNextState(notReadyEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun readyEvent_withoutCredentials_isUnregister() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun readyEvent_withCredentials_isUnregister() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun authOkEvent_isUnregister() {
        val authOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                nullError
        )
        val authOkEvent = AuthOk(authOkMessage)
        val nextState = state.getNextState(authOkEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun authNotOkEvent_isUnregister() {
        val authNotOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                error
        )
        val authNotOkEvent = AuthNotOk(authNotOkMessage)
        val nextState = state.getNextState(authNotOkEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun regOkEvent_isUnregister() {
        val regOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                nullError
        )
        val regOkEvent = RegOK(regOkMessage)
        val nextState = state.getNextState(regOkEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun regNotOkEvent_isUnregister() {
        val regNotOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                error
        )
        val regNotOkEvent = RegNotOk(regNotOkMessage)
        val nextState = state.getNextState(regNotOkEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun schemaOkEvent_isUnregister() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                nullError
        )
        val schemaOkEvent = SchemaOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun schemaNotOkEvent_isUnregister() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                error
        )
        val schemaOkEvent = SchemaNotOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun unregisterOk_isUnregister() {
        val unregisterMessage = KNoTMessageRemoved(
            randomThingId,
            nullError
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Unregister)
    }

    @Test
    fun unregisterNotOk_isUnregister() {
        val unregisterMessage = KNoTMessageRemoved(
            randomThingId,
            error
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Unregister)
    }



}
