package com.example.e_commerce.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.db.entity.ProductEntity
import com.example.e_commerce.data.db.entity.toProductModel
import com.example.e_commerce.data.models.product.ProductCategory
import com.example.e_commerce.data.models.product.ProductModel
import com.example.e_commerce.data.models.product.ProductRemoteModel
import com.example.e_commerce.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseProductViewModel @Inject constructor(private val repository: ProductsRepository) :
    ViewModel() {

    private val _productsLiveData = MutableLiveData<List<ProductModel>>()
    val productsLiveData: LiveData<List<ProductModel>>
        get() = _productsLiveData

    fun cacheProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.cacheProduct(product)
        }
    }

    fun getCachedProducts() {
        viewModelScope.launch {
            repository.getCachedProducts().map { productEntitiesList ->
                productEntitiesList.map { productEntity ->
                    productEntity.toProductModel()
                }
            }.collect { productsModelsList ->
                _productsLiveData.postValue(productsModelsList)
            }
        }
    }

}

