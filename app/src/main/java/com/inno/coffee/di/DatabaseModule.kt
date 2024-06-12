package com.inno.coffee.di

import android.content.Context
import com.inno.common.db.CoffeeRoomDatabase
import com.inno.common.db.dao.DrinksHistoryDao
import com.inno.common.db.dao.FormulaDao
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
    fun provideDrinksHistoryDao(database: CoffeeRoomDatabase): DrinksHistoryDao {
        return database.drinksHistoryDao()
    }

    @Provides
    fun provideFormulaDao(database: CoffeeRoomDatabase): FormulaDao {
        return database.formulaDao()
    }

}