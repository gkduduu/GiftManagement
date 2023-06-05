package com.jhy.giftmanagement.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jhy.giftmanagement.data.GiftCategory
import java.util.Date

@Entity(tableName = "giftinfo")
data class GiftInfoEntity (
    @PrimaryKey(autoGenerate = true)
    val giftID : Long,
    val giftTitle : String,
    val giftContent : String,
    val giftURL : String,
    val giftExpiredDate: Date,
    val giftBarcode : String,
    val giftCategory : GiftCategory
)