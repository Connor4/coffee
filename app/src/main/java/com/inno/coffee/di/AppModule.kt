package com.inno.coffee.di

import android.app.Application
import com.inno.coffee.ui.notice.GlobalDialogManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGlobalDialogManager(application: Application): GlobalDialogManager {
        return GlobalDialogManager(application)
    }

}