package com.example.e_commerce.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.e_commerce.data.db.AppDatabase
import com.example.e_commerce.data.db.dao.ProductDao
import com.example.e_commerce.data.repository.ProductsLocalDataSource
import com.example.e_commerce.data.repository.ProductsRepository
import com.example.e_commerce.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, AppDatabase::class.java,
        "ecommerce_db"
    ).build()

    @Provides
    @Singleton
    fun providesProductDao(db: AppDatabase) = db.getProductDao()


    @Provides
    @Singleton
    fun providesProductsLocalDataSource(dao: ProductDao) = ProductsLocalDataSource(dao)

    @Provides
    @Singleton
    fun providesProductsRepository(localDataSource: ProductsLocalDataSource) =
        ProductsRepository(localDataSource)


}