package com.example.e_commerce.di

import com.example.e_commerce.data.db.dao.ProductDao
import com.example.e_commerce.data.repository.ProductsLocalDataSource
import com.example.e_commerce.data.repository.ProductsRemoteDataSource
import com.example.e_commerce.data.repository.ProductsRepository
import com.example.e_commerce.networking.EcommerceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesProductsLocalDataSource(dao: ProductDao) = ProductsLocalDataSource(dao)

    @Provides
    @Singleton
    fun providesProductsRemoteDataSource(service: EcommerceService) = ProductsRemoteDataSource(service)

    @Provides
    @Singleton
    fun providesProductsRepository(localDataSource: ProductsLocalDataSource, remoteDataSource: ProductsRemoteDataSource) =
        ProductsRepository(localDataSource, remoteDataSource)
}