package com.cesar.knot_sdk.works

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.cesar.knot_sdk.LogWrapper
import com.cesar.knot_sdk.knot_messages.KNoTMessageRequestData
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine
import com.cesar.knot_sdk.knot_state_machine.KNoTStateMachine.knotDataManager
import com.google.gson.Gson
import java.lang.Exception

class PublishWork (val context : Context, val workerParams : WorkerParameters)
    : Worker(context, workerParams) {

    val thingId = KNoTStateMachine.getUUID()

    override fun doWork() = try {
        val dataUpdateMessage = Gson().toJson(
            KNoTMessageRequestData(
                thingId.toString(),
                knotDataManager.getDataSensorIds()
            )
        )
        KNoTStateMachine.dataRequestMessageReceived(dataUpdateMessage)

        Result.success()
    } catch (e : Exception) {
        LogWrapper.log("PeriodicPublishWork exception: $e")
        Result.failure()
    }


    override fun onStopped() {
        super.onStopped()
        LogWrapper.log("PeridoicPublishWork onStopped called,")
    }
}
