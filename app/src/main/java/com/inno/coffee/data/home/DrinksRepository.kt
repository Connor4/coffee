package com.inno.coffee.data.home

import com.inno.common.db.dao.UserDao
import com.inno.common.utils.BcryptUtils
import com.inno.common.utils.UserSessionManager
import javax.inject.Inject

class DrinksRepository @Inject constructor(
    private val drinksLocalDataSource: DrinksLocalDataSource,
    private val userDao: UserDao
) {
    val drinksType: List<DrinksModel> = drinksLocalDataSource.drinksTypes

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