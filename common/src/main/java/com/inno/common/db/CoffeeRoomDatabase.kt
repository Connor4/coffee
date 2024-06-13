package com.inno.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.inno.common.db.dao.DrinksHistoryDao
import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.dao.UserDao
import com.inno.common.db.entity.DrinksHistory
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(DrinksHistory::class, Formula::class, User::class), version = 1,
    exportSchema = false)
abstract class CoffeeRoomDatabase : RoomDatabase() {

    abstract fun drinksHistoryDao(): DrinksHistoryDao

    abstract fun formulaDao(): FormulaDao

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: CoffeeRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CoffeeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoffeeRoomDatabase::class.java,
                    "coffee_database"
                )
                    .addCallback(CoffeeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }

    private class CoffeeDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                scope.launch(Dispatchers.IO) {

                }
            }
        }


    }

}