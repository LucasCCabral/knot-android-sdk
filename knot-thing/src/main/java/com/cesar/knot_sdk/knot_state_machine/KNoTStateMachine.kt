package com.cesar.knot_sdk.knot_state_machine

import android.content.Context
import android.content.SharedPreferences
import android.os.Looper
import androidx.work.WorkManager
import com.cesar.knot_sdk.KNoTAMQP
import com.cesar.knot_sdk.KNoTDataManager
import com.cesar.knot_sdk.KNoTMessageParser
import com.cesar.knot_sdk.KNoTMessager
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import android.os.Handler

object KNoTStateMachine {

    var DEFAULT_RABBIT_PORT_NUMBER = 5672
    var HOSTNAME = ""
    var PORT_NUMBER = DEFAULT_RABBIT_PORT_NUMBER
    var USERNAME = ""
    var PASSWORD = ""

    var state : State = Disconnected()
    lateinit var knotMessager : KNoTMessager
    lateinit var knotAMQP : KNoTAMQP
    lateinit var knotDataManager : KNoTDataManager
    val knotMessageParser = KNoTMessageParser()
    lateinit var context : Context
    lateinit var thingName : String
    lateinit var publishDataScheduler : WorkManager

    private val looper = Looper.myLooper()
    private val timerHandler = Handler(looper)
    private val timeoutEvent = Runnable {
        operationResult(KNoTEvent.Timeout)
    }

    var STANDARD_TIMEOUT : Long = 30000

    fun schemaChanged() = false

    val PREF_ID = "preference file key"
    val UUID_PREF = "uuid"
    val TOKEN_PREF = "token"
    val PREF_MISSING = ""

    lateinit var sharedPref : SharedPreferences

    private fun operationResult(event : KNoTEvent) {
        stopTimeout()
        state = state.getNextState(event)
        state.enter()
        startTimeout()
    }

    private fun startTimeout(timeout : Long = STANDARD_TIMEOUT) =
        timerHandler.postDelayed(timeoutEvent, timeout)

    private fun stopTimeout() = timerHandler.removeCallbacks(timeoutEvent)

    fun enterState() {
        state.enter()
        startTimeout()
    }

    fun getUUID() = sharedPref.getString(
        UUID_PREF,
        PREF_MISSING
    )

    fun storeCredentials(res : KNoTMessageRegistered) {
        sharedPref.edit().putString(
            UUID_PREF,
            res.id
        ).apply()

        sharedPref.edit().putString(
            UUID_PREF,
            res.token
        ).apply()
    }

    fun clearCredentials() {
        sharedPref.edit().putString(
            UUID_PREF,
            PREF_MISSING
        ).apply()

        sharedPref.edit().putString(
            TOKEN_PREF,
            PREF_MISSING
        ).apply()
    }

    fun authMessageReceived(authJson : String) {
        val authMessage = knotMessageParser.parseAuthStatus(authJson)
        val authEvent : KNoTEvent
        val thingId = getUUID()

        if (authMessage.id == thingId) {
            authEvent = if(authMessage.error == "null") KNoTEvent.AuthOk(authMessage)
                        else KNoTEvent.AuthNotOk(authMessage)
            operationResult(authEvent)
        }
    }

    fun registerMessageReceived(regJson : String) {
        val regMessage = knotMessageParser.parseDeviceRegistered(regJson)
        val regEvent : KNoTEvent
        val thingId = getUUID()

        if(regMessage.id == thingId) {
            regEvent = if(regMessage.error == "null") KNoTEvent.RegOK(regMessage)
                       else KNoTEvent.RegNotOk(regMessage)
            operationResult(regEvent)
        }
    }

    fun schemaUpdateMessageReceived(schemaJson : String) {
        val schemaMessage = knotMessageParser.parseSchemaStatus(schemaJson)
        val thingId = getUUID()
        val schemaEvent : KNoTEvent

        if (schemaMessage.id == thingId) {
            schemaEvent = if (schemaMessage.error == "null") KNoTEvent.SchemaOk(schemaMessage)
            else KNoTEvent.SchemaNotOk(schemaMessage)
            operationResult(schemaEvent)
        }
    }

    fun unregisterMessageReceived(removedJson : String) {
        val removedMessage = knotMessageParser.parseDeviceRemoved(removedJson)
        val thingId = getUUID()
        val removedEvent : KNoTEvent

        if (removedMessage.id == thingId) {
            removedEvent = KNoTEvent.UnregisterEvent(removedMessage)
            operationResult(removedEvent)
        }
    }

    fun dataRequestMessageReceived(dataPublishJson : String) {
        val dataPublishMessage = knotMessageParser.parseDataRequest(dataPublishJson)
        val thingId = getUUID()
        val dataPublishEvent : KNoTEvent

        if(dataPublishMessage.id == thingId) {
            dataPublishEvent = KNoTEvent.DataRequest(dataPublishMessage)
            operationResult(dataPublishEvent)
        }
    }

    fun dataUpdateMessageReceived(dataUpdateJson : String) {
        val dataUpdateMessage = knotMessageParser.parseDataUpdate(dataUpdateJson)
        val thingId = getUUID()
        val dataUpdateEvent : KNoTEvent

        if(dataUpdateMessage.id == thingId) {
            dataUpdateEvent = KNoTEvent.DataUpdate(dataUpdateMessage)
            operationResult(dataUpdateEvent)
        }
    }
}
