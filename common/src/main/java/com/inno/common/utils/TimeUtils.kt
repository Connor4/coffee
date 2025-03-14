package com.inno.common.utils

import android.app.AlarmManager
import android.content.Context
import android.os.SystemClock
import android.provider.Settings
import android.text.format.DateFormat
import androidx.annotation.RequiresPermission
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.concurrent.thread

class TimeUtils {

    companion object {
        /** 判断系统的时间是否自动获取的 */
        fun isDateTimeAuto(context: Context): Boolean {
            return Settings.Global.getInt(context.contentResolver, Settings.Global.AUTO_TIME,
                1) == 1
        }

        /** 判断系统的时区是否是自动获取的 */
        fun isTimeZoneAuto(context: Context): Boolean {
            return Settings.Global.getInt(context.contentResolver, Settings.Global.AUTO_TIME_ZONE,
                1) == 1
        }

        /** 系统时间是否是使用24小时制 */
        fun is24HourFormat(context: Context): Boolean {
            return DateFormat.is24HourFormat(context)
            // 下面的方式有问题，比如第一次获取时，如果没有设置过，则会使用默认值24，但其实可能当前是使用的12小时制的，好像一般的手机默认都是使用12小时制的
            // return Settings.System.getInt(context.contentResolver, Settings.System.TIME_12_24, 24) == 24
        }

        /** 设置系统的时间是否需要自动获取 */
        fun setDateTimeAuto(context: Context, autoEnabled: Boolean) {
            Settings.Global.putInt(context.contentResolver, Settings.Global.AUTO_TIME,
                if (autoEnabled) 1 else 0)
        }

        /** 设置系统的时区是否自动获取 */
        fun setTimeZoneAuto(context: Context, autoEnabled: Boolean) {
            Settings.Global.putInt(context.contentResolver, Settings.Global.AUTO_TIME_ZONE,
                if (autoEnabled) 1 else 0)
        }

        /** 设置时间是否使用24小时制 */
        @RequiresPermission(android.Manifest.permission.WRITE_SETTINGS)
        fun set24HourFormat(context: Context, is24HourFormat: Boolean) {
            Settings.System.putInt(context.contentResolver, Settings.System.TIME_12_24,
                if (is24HourFormat) 24 else 12)
        }

        /**
         * 设置系统日期，需要有系统签名才可以
         * @param context
         * @param year 年
         * @param month 月，注：一月是从0开始的，所以11代表的是12月。
         * @param day 日
         */
        @RequiresPermission(android.Manifest.permission.SET_TIME)
        fun setDate(context: Context, year: Int, month: Int, day: Int) {
            thread {
                val calendar = Calendar.getInstance()
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month // 注意：月份从0开始的
                calendar[Calendar.DAY_OF_MONTH] = day
                val timeInMillis = calendar.timeInMillis
                if (timeInMillis / 1000 < Int.MAX_VALUE) {
                    (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setTime(
                        timeInMillis)
                }
            }
        }

        /** 设置系统时间，需要有系统签名才可以 */
        @RequiresPermission(android.Manifest.permission.SET_TIME)
        fun setTime(context: Context, hour: Int, minute: Int) {
            thread {
                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = hour
                calendar[Calendar.MINUTE] = minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
                val timeInMillis = calendar.timeInMillis
                if (timeInMillis / 1000 < Int.MAX_VALUE) {
                    (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setTime(
                        timeInMillis)
                }
            }
        }

        @RequiresPermission(android.Manifest.permission.SET_TIME)
        fun setDateAndTime(
            context: Context, year: Int, month: Int, day: Int, hour: Int,
            minute:
            Int,
        ) {
            thread {
                val calendar = Calendar.getInstance()
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = day
                calendar[Calendar.HOUR_OF_DAY] = hour
                calendar[Calendar.MINUTE] = minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
                val timeInMillis = calendar.timeInMillis
                if (timeInMillis / 1000 < Int.MAX_VALUE) {
                    (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setTime(
                        timeInMillis)
                }
            }
        }

        @RequiresPermission(android.Manifest.permission.SET_TIME)
        fun setDateAndTime(context: Context, yearInMillis: Long, hour: Int, minute: Int) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = yearInMillis
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            val timeInMillis = calendar.timeInMillis
            if (timeInMillis / 1000 < Int.MAX_VALUE) {
                (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setTime(
                    timeInMillis)
            }
        }

        /**
         * 设置系统时区
         * 获取以及设置时区用到的都是TimezoneID，它们以字符串的形式存在。
         * 可以用诸如"GMT+05:00", "GMT+0500", "GMT+5:00","GMT+500","GMT+05", and"GMT+5","GMT-05:00"的ID
         * Android系统用的ID一般为:
         * <timezone id="Asia/Shanghai">中国标准时间 (北京)</timezone>
         * <timezone id="Asia/Hong_Kong">香港时间 (香港)</timezone>
         * <timezone id="Asia/Taipei">台北时间 (台北)</timezone>
         * <timezone id="Asia/Seoul">首尔</timezone>
         * <timezone id="Asia/Tokyo">日本时间 (东京)</timezone>
         * */
        @RequiresPermission(android.Manifest.permission.SET_TIME_ZONE)
        fun setTimeZone(context: Context, timeZoneId: String) {
            thread {
                (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setTimeZone(
                    timeZoneId)
            }
            //DO not need send Intent.ACTION_TIMEZONE_CHANGED
            //Because system will send itself, and we do not have permission
        }

        /** 设置时区为上海 */
        @RequiresPermission(android.Manifest.permission.SET_TIME_ZONE)
        fun setAsChinaTimeZone(context: Context) {
            val chinaTimeZoneId = "Asia/Shanghai"
            if (getTimeZoneId() != chinaTimeZoneId) {
                setTimeZone(context, chinaTimeZoneId)
            }
        }

        /** 获取系统当前的时区 */
        fun getTimeZoneId(): String = TimeZone.getDefault().id

        /** 设置系统日期和时间，需要有系统签名才可以 */
        @RequiresPermission(android.Manifest.permission.SET_TIME)
        fun setDateAndTime(millis: Long) {
            thread {
                // 据说AlarmManager.setTime()检测权限之后也是调用SystemClock.setCurrentTimeMillis(millis)来设置时间的
                SystemClock.setCurrentTimeMillis(millis)
            }
        }

        private const val TAG = "TimeUtils"
        private val englishFullPattern = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")
        private val englishDatePattern = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        private val chineseFullPattern = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        private val chineseDatePattern = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        private val hourMinutePattern = DateTimeFormatter.ofPattern("HH:mm")

        fun getFullFormat(date: LocalDateTime = LocalDateTime.now()): String {
            val default = Locale.getDefault()
            Logger.d(TAG, "getDateString() called with: date = $date language = $default")
            val formatter = when (default.language) {
                Locale.ENGLISH.language -> englishFullPattern
                Locale.SIMPLIFIED_CHINESE.language -> chineseFullPattern
                else -> englishFullPattern
            }
            return formatter.format(date)
        }

        fun getYearMonthDayFormat(date: LocalDateTime = LocalDateTime.now()): String {
            val default = Locale.getDefault()
            Logger.d(TAG, "getDateString() called with: date = $date language = $default")
            val formatter = when (default.language) {
                Locale.ENGLISH.language -> englishDatePattern
                Locale.SIMPLIFIED_CHINESE.language -> chineseDatePattern
                else -> englishDatePattern
            }
            return formatter.format(date)
        }

        fun getHourMinuteFormat(time: LocalDateTime = LocalDateTime.now()): String {
            return hourMinutePattern.format(time)
        }

        fun getLocalDateTimeMilli(time: String): Long {
            val localDateTime = LocalDateTime.parse(time)
            return localDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        }

    }

}