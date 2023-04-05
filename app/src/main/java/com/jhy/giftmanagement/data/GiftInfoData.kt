package com.jhy.giftmanagement.data

import java.util.Date

data class GiftInfoData(
    val giftID : Int,
    val giftTitle : String,
    val giftContent : String,
    val giftURL : String,
    val giftExpiredDate: Date,
    val giftBarcode : String,
    val giftCategory : GiftCategory
)
