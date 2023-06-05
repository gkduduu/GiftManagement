package com.jhy.giftmanagement

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.jhy.giftmanagement.db.GiftInfoDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GiftManagementApplication : Application() {
    val database by lazy { LocalDatabase.getDatabase(this) }
}

class LocalDatabase {
    companion object {
        private lateinit var instance: GiftInfoDatabase

        fun getDatabase(context: Context): GiftInfoDatabase {
            synchronized(GiftInfoDatabase::class) {
                if (!::instance.isInitialized) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GiftInfoDatabase::class.java,
                        "giftinfo_database"
                    ).build()
                }
            }
            return instance
        }
    }
}