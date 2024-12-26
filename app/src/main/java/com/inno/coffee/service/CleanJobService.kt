package com.inno.coffee.service

import android.app.job.JobParameters
import android.app.job.JobService
import com.inno.common.utils.Logger


class CleanJobService : JobService() {
    companion object {
        const val TAG = "CleanJobService"
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
    }


}