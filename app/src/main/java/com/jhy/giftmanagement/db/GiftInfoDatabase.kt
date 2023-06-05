package com.jhy.giftmanagement.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GiftInfoEntity::class], version = 1)
abstract class GiftInfoDatabas : RoomDatabase() {
    abstract fun giftInfoDao() : GiftInfoDao
}