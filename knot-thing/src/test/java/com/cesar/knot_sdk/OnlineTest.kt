package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_messages.KNoTMessageAuthenticated
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_messages.KNoTMessageSchemaResp
import com.cesar.knot_sdk.knot_messages.KNoTMessageUnregister
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.Online
import com.cesar.knot_sdk.knot_state_machine.states.Unregister
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import org.junit.Test

class OnlineTest {

    private val randomThingId = "0123456789abcde"
    private val nullOnline = "null"
    private val error = "non null error"
    private val randomToken = "random token"
    private val state = Online()

    @Test
    fun timeoutEvent_isOnline() {
        val timeoutEvent = Timeout
        val nextState = state.getNextState(timeoutEvent)

        assert(nextState is Online)
    }

    @Test
    fun notReadyEvent_isDisconnected() {
        val notReadyEvent = NotReady
        val nextState = state.getNextState(notReadyEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun readyEvent_withoutCredentials_isOnline() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Online)
    }

    @Test
    fun readyEvent_withCredentials_isOnline() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Online)
    }

    @Test
    fun authOkEvent_isOnline() {
        val authOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                nullOnline
        )
        val authOkEvent = AuthOk(authOkMessage)
        val nextState = state.getNextState(authOkEvent)

        assert(nextState is Online)
    }

    @Test
    fun authNotOkEvent_isOnline() {
        val authNotOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                error
        )
        val authNotOkEvent = AuthNotOk(authNotOkMessage)
        val nextState = state.getNextState(authNotOkEvent)

        assert(nextState is Online)
    }

    @Test
    fun regOkEvent_isOnline() {
        val regOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                nullOnline
        )
        val regOkEvent = RegOK(regOkMessage)
        val nextState = state.getNextState(regOkEvent)

        assert(nextState is Online)
    }

    @Test
    fun regNotOkEvent_isOnline() {
        val regNotOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                error
        )
        val regNotOkEvent = RegNotOk(regNotOkMessage)
        val nextState = state.getNextState(regNotOkEvent)

        assert(nextState is Online)
    }

    @Test
    fun schemaOkEvent_isOnline() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                nullOnline
        )
        val schemaOkEvent = SchemaOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Online)
    }

    @Test
    fun schemaNotOkEvent_isOnline() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                error
        )
        val schemaOkEvent = SchemaNotOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Online)
    }

    @Test
    fun unregisterEvent_correctId_isUnregister() {
        val unregisterMessage = KNoTMessageUnregister(
                randomThingId
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Unregister)
    }

}
