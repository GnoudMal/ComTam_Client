package com.vdsl.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.vdsl.myapplication.Class.Product
import com.vdsl.myapplication.Client.RetrofitClient
import com.vdsl.myapplication.Service.AuthApi
import com.vdsl.myapplication.Service.ProductApi

class ProductViewModel : ViewModel() {
    private val productApi = RetrofitClient.instance.create(ProductApi::class.java)
    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val productList = productApi.getProducts()
                _products.value = productList
            } catch (e: Exception) {
                println("check product error: " + e)
            }
        }
    }
}


