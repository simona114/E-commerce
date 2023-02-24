package com.example.e_commerce.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.e_commerce.data.models.product.ProductCategory
import com.example.e_commerce.data.models.product.ProductModel

@Entity(tableName = "products")
class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id: Int?,
    @ColumnInfo(name = "product_title")
    val title: String,
    @ColumnInfo(name = "product_price")
    val price: Double,
    @ColumnInfo(name = "product_category")
    val category: String,
    @ColumnInfo(name = "product_image")
    val image: String
)

fun ProductEntity.toProductModel(): ProductModel {

    val mCategory: ProductCategory = when (category) {
        "electronics" -> ProductCategory.ELECTRONICS
        "jewelry" -> ProductCategory.JEWELRY
        "women_clothing" -> ProductCategory.WOMEN_CLOTHING
        "men_clothing" -> ProductCategory.MEN_CLOTHING
        else -> {ProductCategory.OTHER}
    }

    return ProductModel(title, price, mCategory, image)
}