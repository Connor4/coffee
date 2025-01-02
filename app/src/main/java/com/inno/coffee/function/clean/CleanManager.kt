package com.inno.coffee.function.clean

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
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
import com.inno.coffee.utilities.SWITCH_VALUE
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


object CleanManager {
    private const val TAG = "CleanManager"

    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var dataStore: CoffeeDataStore
    private lateinit var applicationContext: Context

    fun initialize(dataStore: CoffeeDataStore, applicationContext: Context) {
        this.dataStore = dataStore
        this.applicationContext = applicationContext
        activeScheduleJob()
    }

    /**
     * 1.Parse the schedule and extract the times and statuses for each day.
     * 2.Start from the current time and check:
     *  If the current day is active and the current time is within the On and Off period, use it.
     *  If not, proceed to the next day.
     * 3.Repeat until the next active period is found.
     * 4.If no active period exists for the week, handle this case (e.g., return None).
     */
    fun activeScheduleJob() {
        scope.launch {
            val cleanFlag = dataStore.getCoffeePreference(SWITCH_VALUE, 0)
            if (cleanFlag == 0) {
                Logger.d(TAG, "switch off, no schedule clean")
                return@launch
            }
            val move = findNextActiveTimeWindow(cleanFlag)
            scheduleJob(move.first, move.second)
        }
    }

    private suspend fun findNextActiveTimeWindow(flag: Int): Pair<Int, Long> {
        val now = LocalDateTime.now()
        for (i in 0..6) {
            val dayToCheck = now.plusDays(i.toLong())
            Log.d(TAG, "findActiveTime() called with: now = $now, dayToCheck = $dayToCheck" +
                    ", i = $i")
            val target = findDayOfWeek(now, dayToCheck, flag)
            if (target != null) {
                return target
            }
        }
        return Pair(CLEAN_JOB_FLAG_NONE, 0)
    }

    private suspend fun findDayOfWeek(now: LocalDateTime, findDay: LocalDateTime, flag: Int):
            Pair<Int, Long>? {
        return when (findDay.dayOfWeek) {
            DayOfWeek.MONDAY -> checkDaySchedule(flag, 0, now, findDay, STANDBY_MONDAY,
                STANDBY_MONDAY_END)
            DayOfWeek.TUESDAY -> checkDaySchedule(flag, 1, now, findDay, STANDBY_TUESDAY,
                STANDBY_TUESDAY_END)
            DayOfWeek.WEDNESDAY -> checkDaySchedule(flag, 2, now, findDay, STANDBY_WEDNESDAY,
                STANDBY_WEDNESDAY_END)
            DayOfWeek.THURSDAY -> checkDaySchedule(flag, 3, now, findDay, STANDBY_THURSDAY,
                STANDBY_THURSDAY_END)
            DayOfWeek.FRIDAY -> checkDaySchedule(flag, 4, now, findDay, STANDBY_FRIDAY,
                STANDBY_FRIDAY_END)
            DayOfWeek.SATURDAY -> checkDaySchedule(flag, 5, now, findDay, STANDBY_SATURDAY,
                STANDBY_SATURDAY_END)
            DayOfWeek.SUNDAY -> checkDaySchedule(flag, 6, now, findDay, STANDBY_SUNDAY,
                STANDBY_SUNDAY_END)
            null -> Pair(CLEAN_JOB_FLAG_NONE, 0)
        }
    }

    private suspend fun checkDaySchedule(
        flag: Int, flagIndex: Int, now: LocalDateTime, findDay: LocalDateTime, start: String,
        end: String,
    ): Pair<Int, Long>? {
        if (!isFlagSet(flag, flagIndex)) {
            return null
        }
        return findNextMove(now, findDay, start, end, flag)
    }

    private suspend fun findNextMove(
        now: LocalDateTime, findDay: LocalDateTime, start: String, end: String, flag: Int,
    ): Pair<Int, Long>? {
        val startEncodeTime = dataStore.getCoffeePreference(start, DEFAULT_START_TIME)
        val endEncodeTime = dataStore.getCoffeePreference(end, DEFAULT_END_TIME)
        val startLocalDateTime = decodeTime(startEncodeTime).let {
            LocalDateTime.of(findDay.year, findDay.month, findDay.dayOfMonth, it.first, it.second)
        }
        val endLocalDateTime = decodeTime(endEncodeTime).let {
            LocalDateTime.of(findDay.year, findDay.month, findDay.dayOfMonth, it.first, it.second)
        }
        Logger.d(TAG, "findNextMove() called with: now = $now, findDay = $findDay, " +
                "start = $startLocalDateTime, end = $endLocalDateTime")
        return when {
            now.isBefore(startLocalDateTime) -> {
                val between = ChronoUnit.MILLIS.between(now, startLocalDateTime)
                Pair(CLEAN_JOB_FLAG_WAKEUP, between)
            }
            now.isAfter(startLocalDateTime) && now.isBefore(endLocalDateTime) -> {
                val between = ChronoUnit.MILLIS.between(now, endLocalDateTime)
                Pair(CLEAN_JOB_FLAG_SLEEP, between)
            }
            now.isAfter(endLocalDateTime) -> {
                findDayOfWeek(now, now.plusDays(1), flag)
            }
            else -> Pair(CLEAN_JOB_FLAG_NONE, 0)
        }
    }

    private fun scheduleJob(jobId: Int, intervalMillis: Long) {
        Logger.d(TAG, "scheduleJob() called with: jobId = $jobId, intervalMillis = $intervalMillis")
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

    private fun decodeTime(timeValue: Int): Pair<Int, Int> {
        val hour = (timeValue shr 8) and 0xFF
        val minute = timeValue and 0xFF
        return Pair(hour, minute)
    }

    private fun isFlagSet(cleanFlag: Int, flagIndex: Int): Boolean {
        return (cleanFlag and (1 shl flagIndex)) != 0
    }

}