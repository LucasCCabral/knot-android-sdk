package com.cesar.knot_sdk.knot_state_machine.states

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import com.cesar.knot_sdk.knot_messages.KNoTMessageRequestData
import com.cesar.knot_sdk.knot_messages.KNoTMessageUpdateData
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.getUUID
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotMessager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotDataManager
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.publishDataScheduler
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.KNoTEvent.*
import com.cesar.knot_sdk.knot_state_machine.states.base_classes.State
import com.cesar.knot_sdk.works.PublishWork
import java.io.IOException
import java.util.concurrent.TimeUnit

class Online : State() {
    private val TAG_UPDATE_DATA = "tag update data"
    private val PERIODICITY : Long = 15

    override fun enter() {
        val thingId = getUUID()
        val updateData = KNoTMessageUpdateData(
            thingId!!,
            knotDataManager.getAllKNoTDataValues()
        )

        try {
            knotMessager.publishData(updateData)
        } catch (e : IOException) {
            error = NETWORK_ERROR_MESSAGE
        }
        startPublishDataWork()
    }

    fun startPublishDataWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncDataWork = PeriodicWorkRequest.Builder(
            PublishWork::class.java, PERIODICITY, TimeUnit.MINUTES
        )
            .addTag(TAG_UPDATE_DATA)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        publishDataScheduler.enqueueUniquePeriodicWork(
            TAG_UPDATE_DATA,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncDataWork
        )
    }

   fun getData(requestData : KNoTMessageRequestData) : State {
       val requestedSensorValues = KNoTMessageUpdateData(
           requestData.id,
           knotDataManager.getKNoTDataValues(requestData.sensorIds)
       )

       knotMessager.publishData(requestedSensorValues)
       return this
   }

    private fun updateData(updateData : KNoTMessageUpdateData) : State {
        knotDataManager.updateKNoTData(updateData.data)
        return this
    }

    override fun getNextState(event : KNoTEvent) = when(event) {
        is NotReady        -> Disconnected()
        is Ready           -> this
        is Timeout         -> this
        is RegOK           -> this
        is RegNotOk        -> this
        is AuthNotOk       -> this
        is AuthOk          -> this
        is SchemaOk        -> this
        is SchemaNotOk     -> this
        is UnregisterEvent -> Unregister()
        is DataRequest     -> getData(event.res)
        is DataUpdate      -> updateData(event.res)
    }

}
