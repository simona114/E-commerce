package com.example.e_commerce.data.repository

import androidx.lifecycle.LiveData
import com.example.e_commerce.data.db.dao.ProductDao
import com.example.e_commerce.data.db.entity.ProductEntity
import javax.inject.Inject

class ProductsLocalDataSource @Inject constructor(private val dao: ProductDao) {
    suspend fun saveProduct(product:ProductEntity) {
        dao.addProduct(product)
    }
     fun getSavedProducts(): LiveData<List<ProductEntity>> = dao.readAllProducts()

}