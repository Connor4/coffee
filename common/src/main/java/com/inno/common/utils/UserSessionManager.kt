package com.inno.common.utils

import com.inno.common.db.entity.User

object UserSessionManager {

    private var user: User? = null

    fun setUser(user: User) {
        this.user = user
    }

    fun getUser(): User? {
        return user
    }

    fun clearUser() {
        user = null
    }

    fun isLoggedIn(): Boolean {
        return user != null
    }
}