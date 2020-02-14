package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_messages.KNoTMessageAuthenticated
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_messages.KNoTMessageSchemaResp
import com.cesar.knot_sdk.knot_messages.KNoTMessageUnregister
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.Online
import com.cesar.knot_sdk.knot_state_machine.states.Schema
import com.cesar.knot_sdk.knot_state_machine.states.Unregister
import com.cesar.knot_sdk.knot_state_machine.states.Error
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import org.junit.Test

class SchemaTest {

    val randomThingId = "0123456789abcde"
    val nullError = "null"
    val error = "non null error"
    val randomToken = "random token"
    val state = Schema()

    @Test
    fun timeoutEvent_isSchema() {
        val timeoutEvent = Timeout
        val nextState = state.getNextState(timeoutEvent)

        assert(nextState is Schema)
    }

    @Test
    fun notReadyEvent_isDisconnected() {
        val notReadyEvent = NotReady
        val nextState = state.getNextState(notReadyEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun readyEvent_withoutCredentials_isSchema() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Schema)
    }

    @Test
    fun readyEvent_withCredentials_isSchema() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Schema)
    }

    @Test
    fun authOkEvent_schemaNotChanged_isSchema() {
        val authOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                nullError
        )
        val authOkEvent = AuthOk(authOkMessage)
        val nextState = state.getNextState(authOkEvent)

        assert(nextState is Schema)
    }

    @Test
    fun authOkEvent_schemaChanged_isSchema() {
        val authOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                nullError
        )
        val authOkEvent = AuthOk(authOkMessage)
        val nextState = state.getNextState(authOkEvent)

        assert(nextState is Schema)
    }

    @Test
    fun authNotOkEvent_isSchema() {
        val authNotOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                error
        )
        val authNotOkEvent = AuthNotOk(authNotOkMessage)
        val nextState = state.getNextState(authNotOkEvent)

        assert(nextState is Schema)
    }

    @Test
    fun regOkEvent_isSchema() {
        val regOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                nullError
        )
        val regOkEvent = RegOK(regOkMessage)
        val nextState = state.getNextState(regOkEvent)

        assert(nextState is Schema)
    }

    @Test
    fun regNotOkEvent_isSchema() {
        val regNotOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                error
        )
        val regNotOkEvent = RegNotOk(regNotOkMessage)
        val nextState = state.getNextState(regNotOkEvent)

        assert(nextState is Schema)
    }

    @Test
    fun schemaOkEvent_isOnline() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                nullError
        )
        val schemaOkEvent = SchemaOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Online)
    }

    @Test
    fun schemaNotOkEvent_isError() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                error
        )
        val schemaOkEvent = SchemaNotOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Error)
    }

    @Test
    fun unregisterEvent_isUnregister() {
        val unregisterMessage = KNoTMessageUnregister(
                randomThingId
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Unregister)
    }


}
