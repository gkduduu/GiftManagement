package com.jhy.giftmanagement.repo

import com.jhy.giftmanagement.db.GiftInfoDao
import javax.inject.Inject

class GiftInfoRepository @Inject constructor(private val giftInfoDao: GiftInfoDao) {
    suspend fun getAllGift() = giftInfoDao.getAllGift()
}