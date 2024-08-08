package com.inno.common.di

import android.content.Context
import com.inno.common.db.CoffeeRoomDatabase
import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.dao.ProductHistoryDao
import com.inno.common.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CoffeeRoomDatabase {
        return CoffeeRoomDatabase.getDatabase(context, CoroutineScope(SupervisorJob()))
    }

    @Provides
    fun provideDrinksHistoryDao(database: CoffeeRoomDatabase): ProductHistoryDao {
        return database.drinksHistoryDao()
    }

    @Provides
    fun provideFormulaDao(database: CoffeeRoomDatabase): FormulaDao {
        return database.formulaDao()
    }

    @Provides
    fun provideUserDao(database: CoffeeRoomDatabase): UserDao {
        return database.userDao()
    }

}