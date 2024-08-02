package com.inno.common.utils

import android.util.Log
import com.inno.common.BuildConfig

object Logger {
    private var debuggable = BuildConfig.DEBUG

    fun isDebuggable(debuggable: Boolean) {
        Logger.debuggable = debuggable
    }

    fun d(msg: String) {
        if (debuggable) {
            Log.d("hello", msg)
        }
    }

    fun lengthy(tag: String, msg: String) {
        d(tag, msg)
    }

    fun d(tag: String, msg: String) {
        if (debuggable) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (debuggable) {
            Log.e(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (debuggable) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (debuggable) {
            Log.w(tag, msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (debuggable) {
            Log.v(tag, msg)
        }
    }
}