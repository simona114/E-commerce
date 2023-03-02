package com.example.e_commerce.data.repository

import com.example.e_commerce.data.db.entity.ProductEntity
import com.example.e_commerce.data.models.product.ProductRemoteModel
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val localDataSource: ProductsLocalDataSource, private val remoteDataSource: ProductsRemoteDataSource) {

    suspend fun cacheProduct(product:ProductEntity){
        localDataSource.saveProduct(product)
    }
     fun getCachedProducts() = localDataSource.getSavedProducts()

     suspend fun getProducts() = remoteDataSource.getProducts()
}