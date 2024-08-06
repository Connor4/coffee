package com.inno.coffee.di

import com.inno.coffee.viewmodel.firstinstall.DefaultSettingRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DefaultSettingRepositoryEntryPoint {
    fun defaultSettingRepository(): DefaultSettingRepository
}