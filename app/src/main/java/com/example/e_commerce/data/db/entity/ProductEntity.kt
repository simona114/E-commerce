package com.example.e_commerce.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id: Int?,
    @ColumnInfo(name = "product_price")
    val price:Double,
    @ColumnInfo(name = "product_category")
    val category:String
)