package com.example.e_commerce.data.repository

import com.example.e_commerce.data.db.entity.ProductEntity
import javax.inject.Inject

class ProductsRepository @Inject constructor(val localDataSource: ProductsLocalDataSource) {

    suspend fun cacheProduct(product:ProductEntity){
        localDataSource.saveProduct(product)
    }
     fun getCachedProducts() = localDataSource.getSavedProducts()
}