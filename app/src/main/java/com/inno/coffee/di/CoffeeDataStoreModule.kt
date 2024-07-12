package com.inno.coffee.di

import android.content.Context
import com.inno.coffee.utilities.CoffeeDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoffeeDataStoreModule {

    @Provides
    @Singleton
    fun provideCoffeeDataStore(@ApplicationContext context: Context): CoffeeDataStore {
        return CoffeeDataStore(context)
    }

}