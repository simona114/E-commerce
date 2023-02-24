package com.example.e_commerce.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.e_commerce.data.db.entity.ProductEntity

@Dao
interface ProductDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY product_id ASC")
    fun readAllProducts(): LiveData<List<ProductEntity>>

//    suspend fun readAllProducts(): LiveData<List<ProductEntity>>
//    Dao functions that have a suspend modifier must not return a deferred/async type (androidx.lifecycle.LiveData). Most probably this is an error. Consider changing the return type or removing the suspend modifier.
}