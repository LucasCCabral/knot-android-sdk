package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_messages.KNoTMessageAuthenticated
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_messages.KNoTMessageRemoved
import com.cesar.knot_sdk.knot_messages.KNoTMessageRequestData
import com.cesar.knot_sdk.knot_messages.KNoTMessageSchemaResp
import com.cesar.knot_sdk.knot_messages.KNoTMessageUnregister
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.PREF_MISSING
import com.cesar.knot_sdk.knot_state_machine.states.Authenticating
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.Registering
import com.cesar.knot_sdk.knot_state_machine.states.Unregister
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Test

class DisconnectedTest {

    val randomThingId = "0123456789abcde"
    val nullError = "null"
    val error = "non null error"
    val randomToken = "random token"
    val state = Disconnected()

    @Test
    fun timeoutEvent_isDisconnected() {
        val timeoutEvent = Timeout
        val nextState = state.getNextState(timeoutEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun notReadyEvent_isDisconnected() {
        val notReadyEvent = NotReady
        val nextState = state.getNextState(notReadyEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun readyEvent_withoutCredentials_isRegistered() {
        val readyEvent = Ready

        mockkObject(KNoTStateMachine)
        every { KNoTStateMachine.getUUID() } returns PREF_MISSING

        val nextState = state.getNextState(readyEvent)
        assert(nextState is Registering)
    }

    @Test
    fun readyEvent_withCredentials_isAuthenticating() {
        val readyEvent = Ready
        val validCredential = "valid credential"

        mockkObject(KNoTStateMachine)
        every { KNoTStateMachine.getUUID() } returns validCredential

        val nextState = state.getNextState(readyEvent)
        assert(nextState is Authenticating)
    }

    @Test
    fun authOkEvent_isDisconnected() {
        val authOkMessage = KNoTMessageAuthenticated(
            randomThingId,
            nullError
        )
        val authOkEvent = AuthOk(authOkMessage)
        val nextState = state.getNextState(authOkEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun authNotOkEvent_isDisconnected() {
        val authNotOkMessage = KNoTMessageAuthenticated(
            randomThingId,
            error
        )
        val authNotOkEvent = AuthNotOk(authNotOkMessage)
        val nextState = state.getNextState(authNotOkEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun regOkEvent_isDisconnected() {
        val regOkMessage = KNoTMessageRegistered(
            randomThingId,
            randomToken,
            nullError
        )
        val regOkEvent = RegOK(regOkMessage)
        val nextState = state.getNextState(regOkEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun regNotOkEvent_isDisconnected() {
        val regNotOkMessage = KNoTMessageRegistered(
            randomThingId,
            randomToken,
            error
        )
        val regNotOkEvent = RegNotOk(regNotOkMessage)
        val nextState = state.getNextState(regNotOkEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun schemaOkEvent_isDisconnected() {
        val schemaOkMessage = KNoTMessageSchemaResp(
            randomThingId,
            nullError
        )
        val schemaOkEvent = SchemaOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun schemaNotOkEvent_isDisconnected() {
        val schemaOkMessage = KNoTMessageSchemaResp(
            randomThingId,
            error
        )
        val schemaOkEvent = SchemaNotOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun unregisterOk_isDisconnected() {
        val unregisterMessage = KNoTMessageRemoved(
            randomThingId,
            nullError
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun unregisterNotOk_isDisconnected() {
        val unregisterMessage = KNoTMessageRemoved(
            randomThingId,
            error
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun dataRequest_isDisconnected() {
        val randomSensorIdList = mutableListOf<Int>()
        val dataRequest = KNoTMessageRequestData(
            randomThingId,
            randomSensorIdList
        )
        val dataRequestEvent = DataRequest(dataRequest)
        val nextState = state.getNextState(dataRequestEvent)

        assert(nextState is Disconnected)
    }

}
