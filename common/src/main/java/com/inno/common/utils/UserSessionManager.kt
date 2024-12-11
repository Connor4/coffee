package com.inno.common.utils

import com.inno.common.db.entity.User
import java.util.concurrent.atomic.AtomicInteger

object UserSessionManager {
    private const val TAG = "UserSessionManager"
    private var loginUser: User? = null
    // because we have two screens, so we have to count for twice
    private val loginCount: AtomicInteger = AtomicInteger(0)
    private val grinderShow: AtomicInteger = AtomicInteger(0)

    fun setUser(user: User) {
        val count = loginCount.incrementAndGet()
        Logger.d(TAG, "setUser() called count = $count user = $user")
        if (count == 1) {
            loginUser = user
        }
    }

    fun getUser(): User? {
        return loginUser
    }

    fun clearUser() {
        val count = loginCount.decrementAndGet()
        Logger.d(TAG, "clearUser() called count = $count")
        if (count == 0) {
            Logger.d(TAG, "clearUser() called clear user = $loginUser")
            loginUser = null
        }
    }

    fun isLoggedIn(): Boolean {
        Logger.d(TAG, "isLoggedIn() called count = ${loginCount.get()}, user = $loginUser")
        return loginUser != null
    }

    fun increaseLoginCount() {
        Logger.d(TAG, "increaseLoginCount() called count = ${loginCount.get()}")
        loginCount.incrementAndGet()
    }


}