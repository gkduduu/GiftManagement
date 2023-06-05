package com.jhy.giftmanagement.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface GiftInfoDao {
    @Query("SELECT * FROM giftinfo")
    fun getAllGift(): List<GiftInfoEntity>

    @Insert
    fun insertGift(giftInfo: GiftInfoEntity)

    @Delete
    fun deleteGift(giftInfo: GiftInfoEntity)
}