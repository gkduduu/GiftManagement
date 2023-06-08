package com.jhy.giftmanagement.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GiftInfoDao {
    @Query("SELECT * FROM giftinfo")
    suspend fun getAllGift(): List<GiftInfo>

    @Insert
    suspend fun insertGift(giftInfo: GiftInfo)

    @Delete
    suspend fun deleteGift(giftInfo: GiftInfo)
}