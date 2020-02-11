package com.cesar.knot_sdk

import com.cesar.knot_sdk.knot_data.KNoTData
import com.cesar.knot_sdk.knot_data.KNoTSchema
import com.cesar.knot_sdk.knot_messages.KNoTMessageDataItem

class KNoTDataManager {

    val knotDataPool = mutableListOf<KNoTData<Any>>()

    fun addSensor(knotData : KNoTData<Any>) = knotDataPool.add(knotData)

    fun getDataSensorIds() : List<Int> {
        val sensorIds = mutableListOf<Int>()
        knotDataPool.forEach {
            sensorIds.add(it.getSensorId())
        }
        return sensorIds
    }

    private fun getKNoTDataItem(id : Int) : KNoTData<Any> {
        knotDataPool.forEach { knotData ->
            if (knotData.getSensorId() == id) return knotData
        }
        return knotDataPool[0]
    }

    fun updateKNoTData(knotData : List<KNoTMessageDataItem>) {
        knotData.forEach {
            val knoTDataItem = getKNoTDataItem(it.sensorId)
            knoTDataItem.updateValue(it)
        }
    }

    fun checkKNoTDataValues() : List<KNoTData<Any>>{
       val changedData = mutableListOf<KNoTData<Any>>()
        knotDataPool.forEach {
            if(it.checkValue()) {
              changedData.add(it)
            }
        }
        return changedData
    }

    fun getKNoTDataSchemas() : List<KNoTSchema> {
        val schemaList = mutableListOf<KNoTSchema>()
        knotDataPool.forEach {
            schemaList.add(it.schema)
        }
        return schemaList
    }

    fun getAllKNoTDataValues() : List<KNoTMessageDataItem> {
        val valueList = mutableListOf<KNoTMessageDataItem>()
        knotDataPool.forEach {
            valueList.add(
                it.toKNoTMessageData()
            )
        }
        return valueList
    }

    fun getKNoTDataValues(sensorsId : List<Int>) : List<KNoTMessageDataItem> {
        val valueList = mutableListOf<KNoTMessageDataItem>()
        sensorsId.forEach {
            valueList.add(
                getKNoTDataItem(it).toKNoTMessageData()
            )
        }
        return valueList
    }

    fun getKNoTDataMap() : Map<Int, Int> {
        val knotSensorData = mutableMapOf<Int, Int>()
        knotDataPool.forEach {
            knotSensorData[it.getSensorId()] = it.getSensorType()
        }
        return knotSensorData
    }
}
