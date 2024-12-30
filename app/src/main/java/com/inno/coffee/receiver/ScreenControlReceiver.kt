package com.inno.coffee.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.os.SystemClock
import com.inno.coffee.utilities.ACTION_SLEEP
import com.inno.coffee.utilities.ACTION_WAKEUP
import com.inno.common.utils.Logger
import java.lang.reflect.Method

class ScreenControlReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Logger.d("ScreenControlReceiver", "onReceive() called with: action = $action")
        val powerManager = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "com.inno" +
                    ".coffee::WakeLock")

        when (action) {
            ACTION_WAKEUP -> {
                if (!wakeLock.isHeld) {
                    wakeLock.acquire(3000L)
                    if (wakeLock.isHeld) {
                        wakeLock.release()
                    }
                }
            }
            ACTION_SLEEP -> {
                try {
                    val method: Method? =
                        powerManager.javaClass.getMethod("goToSleep", Long::class.javaPrimitiveType)
                    method?.invoke(powerManager, SystemClock.uptimeMillis())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}