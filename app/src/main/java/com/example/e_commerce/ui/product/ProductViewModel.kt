package com.example.e_commerce.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.db.entity.ProductEntity
import com.example.e_commerce.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductsRepository) :
    ViewModel() {

    //todo: create mappers for product
    lateinit var productsLiveData: LiveData<List<ProductEntity>>

    fun cacheProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.cacheProduct(product)
        }
    }

    fun getCachedProducts() {
        viewModelScope.launch {
            productsLiveData = repository.getCachedProducts()

        }
    }


}

