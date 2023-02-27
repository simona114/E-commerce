package com.example.e_commerce.ui.product

import androidx.lifecycle.MutableLiveData
import com.example.e_commerce.data.models.product.ProductModel
import com.example.e_commerce.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(val repository: ProductsRepository) :
    BaseProductViewModel(repository) {

    val latestQuery = MutableLiveData<String>()
    val filteredProductsLiveData: MutableLiveData<List<ProductModel>?> = MutableLiveData()

    init {
        getCachedProducts()
    }


    fun filterProducts() {
        if (latestQuery.value.isNullOrEmpty()) {
            filteredProductsLiveData.postValue(productsLiveData.value)

        } else {

            val filteredData = productsLiveData.value?.filter { product ->
                product.title.contains(
                    latestQuery.value!!,
                    ignoreCase = true
                )
            }

            filteredProductsLiveData.postValue(filteredData)
        }
    }
}