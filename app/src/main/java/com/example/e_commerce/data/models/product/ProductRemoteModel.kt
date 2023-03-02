package com.example.e_commerce.data.models.product

import com.example.e_commerce.R
import com.example.e_commerce.data.db.entity.ProductEntity
import com.google.gson.annotations.SerializedName

class ProductRemoteModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("image")
    val image: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("rating")
    val rating: RatingModel,
)

fun ProductRemoteModel.toProductModel(): ProductModel {

    val mCategory: ProductCategory = when (category) {
        "electronics" -> ProductCategory.ELECTRONICS
        "jewelery" -> ProductCategory.JEWELRY
        "women's clothing" -> ProductCategory.WOMEN_CLOTHING
        "men's clothing" -> ProductCategory.MEN_CLOTHING
        else -> ProductCategory.OTHER
    }
    return ProductModel(title, price, mCategory, image)
}

fun ProductRemoteModel.toProductEntity() :ProductEntity = ProductEntity(id, title, price, category, image)