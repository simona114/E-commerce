package com.example.e_commerce.ui.home

import android.util.Log
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
import retrofit2.HttpException
import java.io.IOException
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
            try {
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
            catch (e:Exception){
                when (e) {
                    is IOException -> e.message?.let {
                        Log.e(
                            HomeViewModel::class.simpleName,
                            it
                        )
                    }

                    else -> {
                        e.message?.let {
                            Log.e(
                                HomeViewModel::class.simpleName,
                                it
                            )
                        }
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    fun getProductsFromSelectedCategories() {
        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {

                when (e) {
                    is HttpException -> e.message?.let {
                        Log.e(
                            HomeViewModel::class.simpleName,
                            it
                        )
                    }

                    else -> {
                        e.message?.let {
                            Log.e(
                                HomeViewModel::class.simpleName,
                                it
                            )
                        }
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun cacheRemoteProduct(product: ProductRemoteModel) {
        cacheProduct(product.toProductEntity())
    }
}

