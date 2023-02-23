package com.example.e_commerce.di

import android.content.Context
import androidx.room.Room
import com.example.e_commerce.data.db.AppDatabase
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
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun providesProductDao(db:AppDatabase) = db.getProductDao()

}