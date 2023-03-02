package com.example.e_commerce.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.e_commerce.data.db.entity.ProductEntity
import com.example.e_commerce.data.models.product.ProductRemoteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: ProductEntity)

    @Query("SELECT * FROM products ORDER BY product_id ASC")
    fun readAllProducts(): Flow<List<ProductEntity>>
}