package com.inno.common.utils

import com.inno.common.db.entity.User
import java.util.concurrent.atomic.AtomicInteger

object UserSessionManager {

    private var user: User? = null
    private val loginCount: AtomicInteger = AtomicInteger(0)

    fun setUser(user: User) {
        if (loginCount.get() == 0) {
            this.user = user
        } else {
            loginCount.incrementAndGet()
        }
    }

    fun getUser(): User? {
        return user
    }

    fun clearUser() {
        if (loginCount.decrementAndGet() == 0) {
            user = null
        }
    }

    fun isLoggedIn(): Boolean {
        return user != null
    }
}