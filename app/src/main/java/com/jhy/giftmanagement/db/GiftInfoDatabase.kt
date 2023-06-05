package com.jhy.giftmanagement.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GiftInfoEntity::class], version = 1)
abstract class GiftInfoDatabase : RoomDatabase() {
    abstract fun giftInfoDao() : GiftInfoDao
}