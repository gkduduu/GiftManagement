package com.jhy.giftmanagement.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GiftInfoEntity::class], version = 1)
abstract class GiftInfoDatabase : RoomDatabase() {
    abstract fun giftInfoDao(): GiftInfoDao

    companion object {
        @Volatile
        private var INSTANCE: GiftInfoDatabase? = null

        fun getDatabase(context: Context): GiftInfoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GiftInfoDatabase::class.java,
                    "giftinfo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}