package com.inno.coffee.function

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.inno.coffee.service.CleanJobService
import com.inno.common.utils.Logger

object CleanManager {
    private const val TAG = "CleanManager"
    private var applicationContext: Context? = null

    fun init(context: Context?) {
        require(context != null) { "packageContext cannot be null" }
        applicationContext = context.applicationContext

    }

    fun scheduleJob(dayOfWeek: Int, intervalMillis: Long) {
        Logger.d(TAG,
            "scheduleJob() called with: dayOfWeek = $dayOfWeek, intervalMillis = $intervalMillis")
        val scheduler = applicationContext?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as
                JobScheduler
        val componentName = ComponentName("com.inno.coffee", CleanJobService::class.java.getName())
        val jobInfo = JobInfo.Builder(dayOfWeek, componentName)
            .setRequiresCharging(false) // 是否需要充电状态
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE) // 是否需要网络
            .setPersisted(true) // 任务是否在设备重启后保留
            .setMinimumLatency(intervalMillis)
            .setOverrideDeadline(intervalMillis + 5000)
            .build()

        val result = scheduler.schedule(jobInfo)
        if (result == JobScheduler.RESULT_SUCCESS) {
            Logger.d(TAG, "Job scheduled successfully for day: $dayOfWeek")
        } else {
            Logger.e(TAG, "Job scheduling failed for day: $dayOfWeek")
        }
    }

}