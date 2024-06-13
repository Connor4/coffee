package com.inno.coffee.data.settings.permissions

import com.inno.common.db.dao.UserDao
import com.inno.common.db.entity.User
import com.inno.common.utils.BcryptUtils
import com.inno.common.utils.UserSessionManager
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun registerUser(username: String, password: String, roleId: Int, permissionId: Long) {
        val hashedPassword = BcryptUtils.hashPassword(password)
        val user = User(username, hashedPassword, roleId, permissionId)
        userDao.insertUser(user)
    }

    suspend fun authenticateUser(username: String, password: String): Boolean {
        val user = userDao.getUserByUserName(username)
        return user?.let {
            val checkPassword = BcryptUtils.checkPassword(password, it.passwordHash)
            if (checkPassword) {
                UserSessionManager.setUser(user)
            }
            checkPassword
        } ?: false
    }

}