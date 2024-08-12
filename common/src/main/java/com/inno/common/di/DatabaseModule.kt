package com.inno.common.di

import android.content.Context
import com.inno.common.db.CoffeeRoomDatabase
import com.inno.common.db.dao.CleanHistoryDao
import com.inno.common.db.dao.ErrorHistoryDao
import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.dao.MaintenanceHistoryDao
import com.inno.common.db.dao.ProductCountDao
import com.inno.common.db.dao.ProductHistoryDao
import com.inno.common.db.dao.RinseHistoryDao
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
        return database.productHistoryDao()
    }

    @Provides
    fun provideFormulaDao(database: CoffeeRoomDatabase): FormulaDao {
        return database.formulaDao()
    }

    @Provides
    fun provideUserDao(database: CoffeeRoomDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideProductCountDao(database: CoffeeRoomDatabase): ProductCountDao {
        return database.productCountDao()
    }

    @Provides
    fun provideRinseHistoryDao(database: CoffeeRoomDatabase): RinseHistoryDao {
        return database.rinseHistoryDao()
    }

    @Provides
    fun provideCleanHistoryDao(database: CoffeeRoomDatabase): CleanHistoryDao {
        return database.cleanHistoryDao()
    }

    @Provides
    fun provideErrorHistoryDao(database: CoffeeRoomDatabase): ErrorHistoryDao {
        return database.errorHistoryDao()
    }

    @Provides
    fun provideMaintenanceHistoryDao(database: CoffeeRoomDatabase): MaintenanceHistoryDao {
        return database.maintenanceHistoryDao()
    }

}