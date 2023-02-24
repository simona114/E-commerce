package com.example.e_commerce.data.repository

import com.example.e_commerce.data.db.dao.ProductDao
import com.example.e_commerce.data.db.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsLocalDataSource @Inject constructor(private val dao: ProductDao) {
    suspend fun saveProduct(product:ProductEntity) {
        dao.addProduct(product)
    }
      fun getSavedProducts(): Flow<List<ProductEntity>> = dao.readAllProducts()

}