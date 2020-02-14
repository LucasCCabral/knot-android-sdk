package com.cesar.knot_sdk.knot_state_machine

import android.content.Context
import android.content.SharedPreferences
import com.cesar.knot_sdk.KNoTAMQP
import com.cesar.knot_sdk.KNoTDataManager
import com.cesar.knot_sdk.KNoTMessageParser
import com.cesar.knot_sdk.KNoTMessager
import com.cesar.knot_sdk.knot_messages.KNoTMessageRegistered
import com.cesar.knot_sdk.knot_state_machine.states.Disconnected
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State.Companion.PROCEED_MESSAGE

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

    fun schemaChanged() = false

    val PREF_ID = "preference file key"
    val UUID_PREF = "uuid"
    val TOKEN_PREF = "token"
    val PREF_MISSING = ""

    lateinit var sharedPref : SharedPreferences

    fun operationResult(message : String = PROCEED_MESSAGE) {
    }

    private fun waitForResponse() {
        TODO("This method will create a timer that checks for a timeout for the request")
    }

    fun enterState() {
        state.enter()
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

}
