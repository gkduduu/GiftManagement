package com.jhy.giftmanagement.repo

import com.jhy.giftmanagement.db.GiftInfo
import com.jhy.giftmanagement.db.GiftInfoDao
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GiftInfoRepository @Inject constructor(private val giftInfoDao: GiftInfoDao) {
    fun getAllGift() = giftInfoDao.getAllGift()
    suspend fun insertGift(giftInfo: GiftInfo) = giftInfoDao.insertGift(giftInfo)
}