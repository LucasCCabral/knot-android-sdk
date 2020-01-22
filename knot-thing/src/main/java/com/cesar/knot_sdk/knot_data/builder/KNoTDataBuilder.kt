package com.cesar.knot_sdk.knot_data.builder

import com.cesar.knot_sdk.knot_data.KNoTData
import com.cesar.knot_sdk.knot_data.KNoTSchema
import com.cesar.knot_sdk.knot_data.KNoTConfig
import com.cesar.knot_sdk.knot_data.KNoTValue

class KNoTDataBuilder <T> {

    lateinit var lowerBound : KNoTValue<T>
    lateinit var upperBound : KNoTValue<T>
    lateinit var value : KNoTValue<T>
    private var sensorId : Int = 0
    private var valueType : Int = 0
    private var unit : Int = 0
    private var typeId : Int = 0
    private var timeSec : Int = 0
    private var lowerThreshold : Boolean = false
    private var upperThreshold : Boolean = false
    private var changeFlag : Boolean = false
    lateinit var name : String

    fun build() : KNoTData<T> {
        val config = KNoTConfig(
            lowerBound,
            upperBound,
            timeSec,
            lowerThreshold,
            upperThreshold,
            changeFlag
        )

        val schema = KNoTSchema(sensorId, valueType, unit, typeId, name)

        return KNoTData(config = config, schema = schema, value = value)
    }

    fun setName(sensorName : String) : KNoTDataBuilder<T> {
        this.name = sensorName
        return this
    }

    fun setSensorId(sensorId : Int) : KNoTDataBuilder<T> {
        this.sensorId = sensorId
        return this
    }

    fun setUpperBound(upperBound : KNoTValue<T>) : KNoTDataBuilder<T> {
        this.upperBound = upperBound
        this.upperThreshold = true
        return this
    }

    fun setLowerBound(lowerBound : KNoTValue<T>) : KNoTDataBuilder<T> {
        this.lowerBound = lowerBound
        this.lowerThreshold = true
        return this
    }

    fun setValue(value : KNoTValue<T>) : KNoTDataBuilder<T> {
        this.value = value
        return this
    }

    fun setUnit(knotUnit : Int) : KNoTDataBuilder<T> {
        this.unit = knotUnit
        return this
    }

    fun setTypeId(typeId : Int) : KNoTDataBuilder<T> {
        this.typeId = typeId
        return this
    }

    fun setValueType(valueType : Int) : KNoTDataBuilder<T> {
        this.valueType = valueType
        return this
    }

    fun setChangeFlag(changeFlag : Boolean) : KNoTDataBuilder<T> {
        this.changeFlag = changeFlag
        return this
    }

}
