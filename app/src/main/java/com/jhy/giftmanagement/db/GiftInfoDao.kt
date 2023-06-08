package com.jhy.giftmanagement.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GiftInfoDao {
    @Query("SELECT * FROM giftinfo")
//    suspend fun getAllGift(): List<GiftInfo>
    fun getAllGift(): Flow<List<GiftInfo>>

    @Insert
    suspend fun insertGift(giftInfo: GiftInfo)

    @Delete
    suspend fun deleteGift(giftInfo: GiftInfo)
}