package com.example.e_commerce.data.repository

import com.example.e_commerce.data.models.product.ProductRemoteModel
import com.example.e_commerce.networking.EcommerceService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRemoteDataSource @Inject constructor(private val apiClient: EcommerceService) {

       suspend fun getProducts(): List<ProductRemoteModel> = apiClient.getProducts()

}