package com.inno.coffee.viewmodel.firstinstall

import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.dao.UserDao
import com.inno.common.db.entity.User
import com.inno.common.utils.BcryptUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultSettingRepository @Inject constructor(
    private val userDao: UserDao,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend fun insertDefaultUsers(username: String, password: String, role: Int, permission: Int,
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

}