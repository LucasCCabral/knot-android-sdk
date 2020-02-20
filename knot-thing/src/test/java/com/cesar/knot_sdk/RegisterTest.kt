package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_messages.KNoTMessageAuthenticated
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_messages.KNoTMessageRemoved
import com.cesar.knot_sdk.knot_messages.KNoTMessageRequestData
import com.cesar.knot_sdk.knot_messages.KNoTMessageSchemaResp
import com.cesar.knot_sdk.knot_messages.KNoTMessageUnregister
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.Error
import com.cesar.knot_sdk.knot_state_machine.states.Registering
import com.cesar.knot_sdk.knot_state_machine.states.Schema
import com.cesar.knot_sdk.knot_state_machine.states.Unregister
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Test

class RegisterTest {

    private val randomThingId = "0123456789abcde"
    private val nullError = "null"
    private val error = "non null error"
    private val randomToken = "random token"
    private val state = Registering()

    @Test
    fun timeoutEvent_isRegistering() {
        val timeoutEvent = Timeout
        val nextState = state.getNextState(timeoutEvent)

        assert(nextState is Registering)
    }

    @Test
    fun notReadyEvent_isDisconnected() {
        val notReadyEvent = NotReady
        val nextState = state.getNextState(notReadyEvent)

        assert(nextState is Disconnected)
    }

    @Test
    fun readyEvent_withoutCredentials_isRegistering() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Registering)
    }

    @Test
    fun readyEvent_withCredentials_isRegistering() {
        val readyEvent = Ready
        val nextState = state.getNextState(readyEvent)

        assert(nextState is Registering)
    }

    @Test
    fun authOkEvent_isRegistering() {
        val authOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                nullError
        )
        val authOkEvent = AuthOk(authOkMessage)
        val nextState = state.getNextState(authOkEvent)

        assert(nextState is Registering)
    }

    @Test
    fun authNotOkEvent_isRegistering() {
        val authNotOkMessage = KNoTMessageAuthenticated(
                randomThingId,
                error
        )
        val authNotOkEvent = AuthNotOk(authNotOkMessage)
        val nextState = state.getNextState(authNotOkEvent)

        assert(nextState is Registering)
    }

    @Test
    fun regOkEvent_isSchema() {
        val regOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                nullError
        )
        val regOkEvent = RegOK(regOkMessage)

        mockkObject(KNoTStateMachine)
        every { KNoTStateMachine.storeCredentials(regOkMessage) } returns Unit

        val nextState = state.getNextState(regOkEvent)
        assert(nextState is Schema)
    }

    @Test
    fun regNotOkEvent_isRegistering() {
        val regNotOkMessage = KNoTMessageRegistered(
                randomThingId,
                randomToken,
                error
        )
        val regNotOkEvent = RegNotOk(regNotOkMessage)
        val nextState = state.getNextState(regNotOkEvent)

        assert(nextState is Registering)
    }

    @Test
    fun schemaOkEvent_isRegistering() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                nullError
        )
        val schemaOkEvent = SchemaOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Registering)
    }

    @Test
    fun schemaNotOkEvent_isRegistering() {
        val schemaOkMessage = KNoTMessageSchemaResp(
                randomThingId,
                error
        )
        val schemaOkEvent = SchemaNotOk(schemaOkMessage)
        val nextState = state.getNextState(schemaOkEvent)

        assert(nextState is Registering)
    }

    @Test
    fun unregisterOk_isRegistering() {
        val unregisterMessage = KNoTMessageRemoved(
            randomThingId,
            nullError
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Registering)
    }

    @Test
    fun unregisterNotOk_isRegistering() {
        val unregisterMessage = KNoTMessageRemoved(
            randomThingId,
            error
        )
        val unregisterEvent = UnregisterEvent(unregisterMessage)
        val nextState = state.getNextState(unregisterEvent)

        assert(nextState is Registering)
    }

    @Test
    fun dataRequest_isRegistering() {
        val randomSensorIdList = mutableListOf<Int>()
        val dataRequest = KNoTMessageRequestData(
            randomThingId,
            randomSensorIdList
        )
        val dataRequestEvent = DataRequest(dataRequest)
        val nextState = state.getNextState(dataRequestEvent)

        assert(nextState is Registering)
    }
}
