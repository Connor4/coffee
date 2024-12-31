package com.inno.coffee.function.clean

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.inno.coffee.service.CleanJobService
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CleanManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val dataStore: CoffeeDataStore,
) {
    companion object {
        private const val TAG = "CleanManager"
        private const val STANDBY_MONDAY = "clean_standby_monday"
        private const val DEFAULT_START_TIME = 2304
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * 1. find current time, check what' next move
     */
    fun activeScheduleJob() {

    }

    fun scheduleJob(jobId: Int, intervalMillis: Long) {
        scope.launch {
            val monday = dataStore.getCoffeePreference(STANDBY_MONDAY, DEFAULT_START_TIME)
            Logger.d(TAG, "scheduleJob() called $monday")
            delay(5000)

            val scheduler =
                applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val componentName =
                ComponentName(applicationContext.packageName, CleanJobService::class.java.name)
            val jobInfo = JobInfo.Builder(jobId, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setPersisted(true)
                .setMinimumLatency(intervalMillis)
                .setOverrideDeadline(intervalMillis + 5000)
                .build()

            val result = scheduler.schedule(jobInfo)
            if (result == JobScheduler.RESULT_SUCCESS) {
                Logger.d(TAG, "Job scheduled successfully for day: $jobId")
            } else {
                Logger.e(TAG, "Job scheduling failed for day: $jobId")
            }
        }
    }

}