package com.jhy.giftmanagement.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "giftinfo")
data class GiftInfoEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val barcodeNumber: String,
    val expiryDate: String,
    val category: String
)