package com.example.e_commerce.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.db.entity.toProductModel
import com.example.e_commerce.data.models.product.ProductModel
import com.example.e_commerce.data.models.product.ProductRemoteModel
import com.example.e_commerce.data.models.product.toProductEntity
import com.example.e_commerce.data.models.product.toProductModel
import com.example.e_commerce.data.repository.ProductsRepository
import com.example.e_commerce.ui.product.BaseProductViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductsRepository) :
    BaseProductViewModel(repository) {

    private val _filteredProductsLiveData = MutableLiveData<List<ProductModel>>()
    val filteredProductsLiveData: LiveData<List<ProductModel>>
        get() = _filteredProductsLiveData

    val selectedProductCategories = MutableLiveData<List<String>>()

    fun getCachedProductsFromSelectedCategories() {
        viewModelScope.launch {

            if (selectedProductCategories.value.isNullOrEmpty()) {
                repository.getCachedProducts().map { productEntitiesList ->
                    productEntitiesList.map { productEntity ->
                        productEntity.toProductModel()
                    }
                }.collect { productsModelsList ->
                    _filteredProductsLiveData.postValue(productsModelsList)
                }
            } else {
                repository.getCachedProducts().map { productEntitiesList ->
                    productEntitiesList.filter { productEntity ->
                        productEntity.category in selectedProductCategories.value!!
                    }.map { productEntity ->
                        productEntity.toProductModel()
                    }

                }.collect { productsModelsList ->
                    _filteredProductsLiveData.postValue(productsModelsList)
                }
            }
        }
    }

    fun getProductsFromSelectedCategories() {
        viewModelScope.launch {
            if (selectedProductCategories.value.isNullOrEmpty()) {
                val result = repository.getProducts().map { productRemote ->
                    cacheRemoteProduct(productRemote)
                    productRemote.toProductModel()
                }
                _filteredProductsLiveData.postValue(result)


            } else {
                val result = repository.getProducts().filter { productRemote ->
                    productRemote.category in selectedProductCategories.value!!
                }.map { productRemote ->
                    productRemote.toProductModel()
                }
                _filteredProductsLiveData.postValue(result)
            }
        }
    }

    private fun cacheRemoteProduct(product: ProductRemoteModel) {
        cacheProduct(product.toProductEntity())
    }
}

