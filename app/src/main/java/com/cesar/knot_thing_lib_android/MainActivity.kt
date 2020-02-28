package com.cesar.knot_thing_lib_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cesar.knot_sdk.KNoTThingBuilder
import com.cesar.knot_sdk.KNoTTypes.KNOT_TYPE_ID_NONE
import com.cesar.knot_sdk.KNoTTypes.KNOT_UNIT_NOT_APPLICABLE
import com.cesar.knot_sdk.KNoTTypes.KNOT_VALUE_TYPE_INT
import com.cesar.knot_sdk.knot_data.KNoTData
import com.cesar.knot_sdk.knot_data.KNoTValueInt
import com.cesar.knot_sdk.knot_data.builder.KNoTDataBuilder

class MainActivity : AppCompatActivity() {
    val HOSTNAME = "192.168.31.123"
    val PORT_NUMBER = 5672
    val USERNAME = "lucas"
    val PASSWORD = "lucas"
    val THING_NAME = "thing-name"
    val SENSOR_ID = 1
    val SENSOR_NAME = "updateSchemaTest"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lowerBound = KNoTValueInt(SENSOR_ID, 0)
        val upperBound = KNoTValueInt(SENSOR_ID, 10)
        val currValue = KNoTValueInt(SENSOR_ID, 5)

        val knotData = KNoTDataBuilder<KNoTValueInt>()
            .setName(SENSOR_NAME)
            .setSensorId(SENSOR_ID)
            .setValueType(KNOT_VALUE_TYPE_INT)
            .setUnit(KNOT_UNIT_NOT_APPLICABLE)
            .setTypeId(KNOT_TYPE_ID_NONE)
            .setLowerBound(lowerBound)
            .setUpperBound(upperBound)
            .setValue(currValue)
            .build()

        val knotThing = KNoTThingBuilder()
            .setHostname(HOSTNAME)
            .setUsername(USERNAME)
            .setPassword(PASSWORD)
            .setPortNumber(PORT_NUMBER)
            .setContext(applicationContext)
            .setThingName(THING_NAME)
            .addSensor(knotData as KNoTData<Any>)
            .build()

        knotThing.enterState()

    }
}
