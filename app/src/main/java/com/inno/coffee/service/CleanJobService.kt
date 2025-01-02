package com.inno.coffee.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.inno.coffee.function.clean.CleanManager
import com.inno.coffee.utilities.ACTION_SLEEP
import com.inno.coffee.utilities.ACTION_WAKEUP
import com.inno.coffee.utilities.CLEAN_JOB_FLAG_SLEEP
import com.inno.coffee.utilities.CLEAN_JOB_FLAG_WAKEUP
import com.inno.common.utils.Logger


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
        when (jobId) {
            CLEAN_JOB_FLAG_WAKEUP -> sendBroadcast(Intent(ACTION_WAKEUP))
            CLEAN_JOB_FLAG_SLEEP -> {
                sendBroadcast(Intent(ACTION_SLEEP))
                CleanManager.activeScheduleJob()
            }
        }
    }

}