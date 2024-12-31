package com.inno.coffee.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.inno.coffee.utilities.ACTION_SLEEP
import com.inno.coffee.utilities.ACTION_WAKEUP
import com.inno.common.utils.Logger
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CleanJobService : JobService() {
    companion object {
        private const val TAG = "CleanJobService"
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Logger.d(TAG, "onStartJob() called with: params = $params")
        executeTask(params?.jobId)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Logger.d(TAG, "onStopJob() called with: params = $params")
        return false
    }

    private fun executeTask(jobId: Int?) {
        Logger.d(TAG, "executeTask() called with: jobId = $jobId")
        sendBroadcast(Intent(ACTION_SLEEP))
        MainScope().launch {
            delay(3000)
            sendBroadcast(Intent(ACTION_WAKEUP))
        }
    }

}