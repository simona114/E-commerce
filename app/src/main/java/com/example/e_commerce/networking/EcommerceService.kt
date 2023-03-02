package com.example.e_commerce.networking

import com.example.e_commerce.data.models.product.ProductRemoteModel
import kotlinx.coroutines.flow.Flow
import okhttp3.Call
import retrofit2.http.GET

interface EcommerceService {

    @GET("products")
     suspend fun getProducts(): List<ProductRemoteModel>

}