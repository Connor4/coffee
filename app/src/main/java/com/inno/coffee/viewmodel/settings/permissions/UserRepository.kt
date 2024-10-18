package com.inno.coffee.viewmodel.settings.permissions

import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.dao.UserDao
import com.inno.common.db.entity.User
import com.inno.common.utils.BcryptUtils
import com.inno.common.utils.UserSessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend fun registerUser(username: String, password: String, role: Int, permission: Int,
        remark: String): Boolean {
        return withContext(defaultDispatcher) {
            val hashedPassword = BcryptUtils.hashPassword(password)
            val user = User(username, hashedPassword, role, permission, remark)
            try {
                userDao.insertUser(user) != -1L
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun authenticateUser(username: String, password: String): Boolean {
        return withContext(defaultDispatcher) {
            val user = userDao.getUserByUserName(username)
            user?.let {
                val checkPassword = BcryptUtils.checkPassword(password, it.passwordHash)
                if (checkPassword) {
                    UserSessionManager.setUser(user)
                }
                checkPassword
            } ?: false
        }
    }

    suspend fun updateUser(user: User): Boolean {
        return withContext(defaultDispatcher) {
            try {
                userDao.updateUser(user) > 0
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun getUserByUsername(username: String): User? {
        return withContext(defaultDispatcher) {
            userDao.getUserByUserName(username)
        }
    }

    suspend fun deleteUser(user: User): Boolean {
        return withContext(defaultDispatcher) {
            try {
                userDao.deleteUser(user.id) > 0
            } catch (e: Exception) {
                false
            }
        }
    }

    fun getAllUserFlow(): Flow<List<User>> {
        return userDao.getAllUserFlow()
    }

}