package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_data.KNoTData
import com.cesar.knot_sdk.knot_messages.KNoTMessageAuthenticated
import com.cesar.knot_sdk.knot_messages.KNoTMessageDataItem
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_messages.KNoTMessageRemoved
import com.cesar.knot_sdk.knot_messages.KNoTMessageRequestData
import com.cesar.knot_sdk.knot_messages.KNoTMessageSchemaResp
import com.cesar.knot_sdk.knot_messages.KNoTMessageUpdateData
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.Error
import com.cesar.knot_sdk.knot_state_machine.states.Online
import com.cesar.knot_sdk.knot_state_machine.states.Unregister
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.mockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test

class OnlineTest {

    private val randomThingId = "0123456789abcde"
    private val nullError = "null"
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
                nullError
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
                nullError
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
                nullError
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


    @Before
    fun dataRequest_isOnline_Setup() {
        val randomSensorIdList = mutableListOf<Int>()
        val dataRequest = KNoTMessageRequestData(
            randomThingId,
            randomSensorIdList
        )
        val dataManager = mockkClass(KNoTDataManager::class)
        val knotMessager = mockkClass(KNoTMessager::class)

        mockkObject(KNoTStateMachine)
        KNoTStateMachine.knotDataManager = dataManager
        KNoTStateMachine.knotMessager = knotMessager

        every {
            KNoTStateMachine.knotDataManager.getKNoTDataValues(
                randomSensorIdList
            )
        } returns mutableListOf<KNoTMessageDataItem>()

        val requestedSensorValues = KNoTMessageUpdateData(
            dataRequest.id,
            KNoTStateMachine.knotDataManager.getKNoTDataValues(dataRequest.sensorIds)
        )

        every {
            KNoTStateMachine.knotMessager.publishData(
                requestedSensorValues
            )
        } just Runs
    }

    @Test
    fun dataRequest_isOnline() {
        val randomSensorIdList = mutableListOf<Int>()
        val dataRequest = KNoTMessageRequestData(
            randomThingId,
            randomSensorIdList
        )
        val dataRequestEvent = DataRequest(dataRequest)
        val nextState = state.getNextState(dataRequestEvent)

        assert(nextState is Online)
    }

    @After
    fun cleanup() {
        clearAllMocks()
    }

}
