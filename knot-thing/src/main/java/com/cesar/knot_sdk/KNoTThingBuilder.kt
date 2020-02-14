package com.cesar.knot_sdk

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.work.WorkManager
import com.cesar.knot_sdk.knot_data.KNoTData
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.PREF_ID

class KNoTThingBuilder {

    lateinit private var context : Context

    private val sensors = mutableListOf<KNoTData<Any>>()
    private var hostname = ""
    private var portNumber = 5672
    private var username = ""
    private var password = ""
    private var thingName = ""


    fun build() : KNoTStateMachine {
        var stMachine = KNoTStateMachine
        stMachine.HOSTNAME = hostname
        stMachine.PASSWORD = password
        stMachine.USERNAME = username
        stMachine.PORT_NUMBER = portNumber
        stMachine.thingName = thingName
        stMachine.context = context
        stMachine.publishDataScheduler = WorkManager.getInstance(context)
        stMachine.knotDataManager.knotDataPool.addAll(sensors)
        stMachine.knotAMQP = KNoTAMQP(username, password, hostname, portNumber)
        stMachine.sharedPref = context.getSharedPreferences(PREF_ID, MODE_PRIVATE)

        stMachine.knotAMQP = KNoTAMQP(
            username,
            password,
            hostname,
            portNumber
        )

        return stMachine
    }

    fun addSensor(sensor : KNoTData<Any>) : KNoTThingBuilder {
        sensors.add(sensor)
        return this
    }

    fun setHostname(host : String) : KNoTThingBuilder {
        hostname = host
        return this
    }

    fun setPortNumber(port : Int) : KNoTThingBuilder {
        portNumber = port
        return this
    }

    fun setUsername(username : String) : KNoTThingBuilder {
        this.username = username
        return this
    }

    fun setPassword(password : String) : KNoTThingBuilder {
        this.password = password
        return this
    }

    fun setContext(context : Context) : KNoTThingBuilder {
        this.context = context
        return this
    }

    fun setThingName(name : String) : KNoTThingBuilder {
        thingName = name
        return this
    }

}
