package com.jhy.giftmanagement.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jhy.giftmanagement.data.GiftCategory

@Entity(tableName = "giftinfo")
data class GiftInfo (
    val giftTitle : String,
    val giftContent : String,
    val giftURL : String,
    val giftExpiredDate: String,
    val giftBarcode : String,
    val giftCategory : GiftCategory
){
    @PrimaryKey(autoGenerate = true)
    var giftID: Long = 0
}