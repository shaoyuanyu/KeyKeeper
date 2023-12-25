package com.yusy.keykeeper.data.account

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Account::class, UsedUid::class], version = 6, exportSchema = false)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun usedUidDao(): UsedUidDao

    companion object {
        @Volatile
        private var Instance: AccountDatabase? = null

        fun getDatabase(context: Context): AccountDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AccountDatabase::class.java, "account_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}