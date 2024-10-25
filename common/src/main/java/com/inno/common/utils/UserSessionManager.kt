package com.inno.common.utils

import com.inno.common.db.entity.User
import java.util.concurrent.atomic.AtomicInteger

object UserSessionManager {
    private const val TAG = "UserSessionManager"
    private var user: User? = null
    private val loginCount: AtomicInteger = AtomicInteger(0)

    fun setUser(user: User) {
        val count = loginCount.incrementAndGet()
        Logger.d(TAG, "setUser() called count = $count")
        if (count == 0) {
            this.user = user
        }
    }

    fun getUser(): User? {
        return user
    }

    fun clearUser() {
        val count = loginCount.decrementAndGet()
        Logger.d(TAG, "clearUser() called count = $count")
        if (count == 0) {
            user = null
        }
    }

    fun isLoggedIn(): Boolean {
        Logger.d(TAG, "isLoggedIn() called count = ${loginCount.get()}")
        return user != null
    }
}