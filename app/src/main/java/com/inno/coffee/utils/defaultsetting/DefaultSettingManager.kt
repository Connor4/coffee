package com.inno.coffee.utils.defaultsetting

import android.app.Application
import com.inno.coffee.di.DefaultSettingRepositoryEntryPoint
import com.inno.coffee.viewmodel.firstinstall.DefaultSettingRepository
import com.inno.common.db.entity.defaultUsers
import dagger.hilt.android.EntryPointAccessors

object DefaultSettingManager {
    private lateinit var repository: DefaultSettingRepository

    suspend fun init(application: Application) {
        val entryPoint = EntryPointAccessors.fromApplication(
            application,
            DefaultSettingRepositoryEntryPoint::class.java
        )
        repository = entryPoint.defaultSettingRepository()
        insertDefaultUser()
    }

    private suspend fun insertDefaultUser() {
        defaultUsers.forEach {
            repository.insertDefaultUsers(it.username, it.passwordHash, it.role, it.permission,
                it.remark)
        }
    }

    fun resetDefault() {

    }

}