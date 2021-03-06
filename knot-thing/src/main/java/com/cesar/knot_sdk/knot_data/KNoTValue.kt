package com.cesar.knot_sdk.knot_data

/**
 * This interface acts as an abstraction on how to compare two KNoTValues. It departs from the idea
 * that each KNoTValue will have a lower and an upper bound and that the comparision of these limits
 * is intrinsic to it's type.
 * e.g.: the accelerometer should be the one responsible to compare itself.
 * The creation of this class, instead of using Comparator interface, was necessary in order
 * to be compatible with the KNoT protocol.
 */
interface KNoTValue<T> : KNoTIO<T> {
    val sensorId : Int
    /**
     * This method grant's a way to compare if an object is lesser than other object.
     *
     * @return true if the parameter is smaller than some threshold, false otherwise
     */
    fun lesser(knotValue : T) : Boolean
    /**
     * This method grant's a way to compare if an object is greater than other object.
     *
     * @return true if the parameter is greater than some threshold, false otherwise
     */
    fun greater(knotValue : T) : Boolean

    fun getValues() : Any
}
