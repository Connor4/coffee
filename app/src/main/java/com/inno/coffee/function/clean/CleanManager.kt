package com.inno.coffee.function.clean

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.inno.coffee.service.CleanJobService
import com.inno.coffee.utilities.CLEAN_JOB_FLAG_NONE
import com.inno.coffee.utilities.CLEAN_JOB_FLAG_SLEEP
import com.inno.coffee.utilities.CLEAN_JOB_FLAG_WAKEUP
import com.inno.coffee.utilities.DEFAULT_END_TIME
import com.inno.coffee.utilities.DEFAULT_START_TIME
import com.inno.coffee.utilities.STANDBY_FRIDAY
import com.inno.coffee.utilities.STANDBY_FRIDAY_END
import com.inno.coffee.utilities.STANDBY_MONDAY
import com.inno.coffee.utilities.STANDBY_MONDAY_END
import com.inno.coffee.utilities.STANDBY_SATURDAY
import com.inno.coffee.utilities.STANDBY_SATURDAY_END
import com.inno.coffee.utilities.STANDBY_SUNDAY
import com.inno.coffee.utilities.STANDBY_SUNDAY_END
import com.inno.coffee.utilities.STANDBY_THURSDAY
import com.inno.coffee.utilities.STANDBY_THURSDAY_END
import com.inno.coffee.utilities.STANDBY_TUESDAY
import com.inno.coffee.utilities.STANDBY_TUESDAY_END
import com.inno.coffee.utilities.STANDBY_WEDNESDAY
import com.inno.coffee.utilities.STANDBY_WEDNESDAY_END
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import javax.inject.Inject


class CleanManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val dataStore: CoffeeDataStore,
) {
    companion object {
        private const val TAG = "CleanManager"
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * 1. find current time, check what' next move
     */
    fun activeScheduleJob() {
        scope.launch {
            val now = LocalDateTime.now()
            val flag = when (now.dayOfWeek) {
                DayOfWeek.MONDAY -> {
                    findNextMove(now, STANDBY_MONDAY, STANDBY_MONDAY_END)
                }
                DayOfWeek.TUESDAY -> {
                    findNextMove(now, STANDBY_TUESDAY, STANDBY_TUESDAY_END)
                }
                DayOfWeek.WEDNESDAY -> {
                    findNextMove(now, STANDBY_WEDNESDAY, STANDBY_WEDNESDAY_END)
                }
                DayOfWeek.THURSDAY -> {
                    findNextMove(now, STANDBY_THURSDAY, STANDBY_THURSDAY_END)
                }
                DayOfWeek.FRIDAY -> {
                    findNextMove(now, STANDBY_FRIDAY, STANDBY_FRIDAY_END)
                }
                DayOfWeek.SATURDAY -> {
                    findNextMove(now, STANDBY_SATURDAY, STANDBY_SATURDAY_END)
                }
                DayOfWeek.SUNDAY -> {
                    findNextMove(now, STANDBY_SUNDAY, STANDBY_SUNDAY_END)
                }
            }
            scheduleJob(flag, 1000)
        }
    }

    private fun scheduleJob(jobId: Int, intervalMillis: Long) {
        scope.launch {
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

    private suspend fun findNextMove(now: LocalDateTime, start: String, end: String): Int {
        val startEncodeTime = dataStore.getCoffeePreference(start, DEFAULT_START_TIME)
        val endEncodeTime = dataStore.getCoffeePreference(end, DEFAULT_END_TIME)
        val startLocalDateTime = decodeTime(startEncodeTime).let {
            LocalDateTime.of(now.year, now.month, now.dayOfMonth, it.first, it.second)
        }
        val endLocalDateTime = decodeTime(endEncodeTime).let {
            LocalDateTime.of(now.year, now.month, now.dayOfMonth, it.first, it.second)
        }
        return when {
            now.isBefore(startLocalDateTime) -> CLEAN_JOB_FLAG_WAKEUP
            now.isAfter(startLocalDateTime) && now.isBefore(
                endLocalDateTime) -> CLEAN_JOB_FLAG_SLEEP
            else -> CLEAN_JOB_FLAG_NONE
        }
    }

    private fun decodeTime(timeValue: Int): Pair<Int, Int> {
        val hour = (timeValue shr 8) and 0xFF
        val minute = timeValue and 0xFF
        return Pair(hour, minute)
    }

}