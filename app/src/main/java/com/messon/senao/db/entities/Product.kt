package com.messon.senao.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class Product (
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val martShortName: String,
    val martName: String,
    val finalPrice: Int,
    val imageUrl: String,
    val martId: String
)