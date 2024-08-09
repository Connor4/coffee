package com.inno.coffee.function.defaultsetting

import android.app.Application
import com.inno.coffee.di.DefaultSettingRepositoryEntryPoint
import com.inno.coffee.viewmodel.firstinstall.DefaultSettingRepository
import com.inno.common.db.entity.defaultUsers
import dagger.hilt.android.EntryPointAccessors

object DefaultSettingManager {
    private lateinit var repository: DefaultSettingRepository

    fun init(application: Application) {
        val entryPoint = EntryPointAccessors.fromApplication(
            application,
            DefaultSettingRepositoryEntryPoint::class.java
        )
        repository = entryPoint.defaultSettingRepository()
    }

    suspend fun insertDefaultUser() {
        defaultUsers.forEach {
            repository.insertDefaultUsers(it.username, it.passwordHash, it.role, it.permission,
                it.remark)
        }
    }

    suspend fun insertDefaultProductType() {
        // 预设饮品，保证统计展示页面可以获取到0的数据

    }

    suspend fun resetDefault() {
        insertDefaultUser()
        insertDefaultProductType()
    }

}