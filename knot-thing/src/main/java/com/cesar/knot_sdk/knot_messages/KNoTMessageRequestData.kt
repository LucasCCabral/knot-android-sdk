package com.cesar.knot_sdk.knot_messages

/**
 * This is the class that deserialize the response of the authentication message.
 * @property id the Thing unique identifier
 * @property sensorIds a list with IDs of the sensor to send last value
 */
data class KNoTMessageRequestData(
    val id : String,
    val sensorIds: List<Int>
)
