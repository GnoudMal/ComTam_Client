package com.vdsl.myapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdsl.myapplication.Class.AddToCartRequest
import com.vdsl.myapplication.Class.CartItem
import com.vdsl.myapplication.Class.UpdateCartRequest
import com.vdsl.myapplication.Client.RetrofitClient
import com.vdsl.myapplication.Service.CartService
import com.vdsl.myapplication.Screen.countItemsByCategory
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val cartApi = RetrofitClient.instance.create(CartService::class.java)
    private val _cartItems = MutableLiveData<List<CartItem>?>()
    val cartItems: LiveData<List<CartItem>?> get() = _cartItems

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    private val _categoryCounts = MutableLiveData<Map<String, Int>>()
    val categoryCounts: LiveData<Map<String, Int>> get() = _categoryCounts


    fun addToCart(token: String?, productId: String, quantity: Int, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = cartApi.addToCart(
                    token = "Bearer $token",
                    addToCartRequest = AddToCartRequest(productId, quantity)
                )
                onSuccess(response.msg)
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun getCart(token: String?, onSuccess: (List<CartItem>) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = cartApi.getCart(token = "Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let { cartItems ->
                        _cartItems.value = cartItems
                        calculateTotalPrice(cartItems)
                        updateCategoryCounts()
                        onSuccess(cartItems)
                    } ?: onError("Giỏ hàng trống")
                } else {
                    onError("Không thể lấy giỏ hàng: ${response.errorBody()?.string() ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun updateCartItem(token: String?, cartItem: CartItem, newQuantity: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val productId = cartItem.product._id
        viewModelScope.launch {
            try {
                val updateCartRequest = UpdateCartRequest(productId = productId, quantity = newQuantity)

                val response = cartApi.updateCart("Bearer $token", updateCartRequest)

                if (response.isSuccessful) {
                    val updatedList = _cartItems.value?.map {
                        if (it.product._id == productId) it.copy(quantity = newQuantity) else it
                    }
                    _cartItems.value = updatedList
                    updatedList?.let {
                        calculateTotalPrice(it)
                        updateCategoryCounts()
                    }

                    onSuccess()
                } else {
                    onError("Không thể cập nhật giỏ hàng: ${response.code()} - ${response.errorBody()?.string() ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun updateCategoryCounts() {
        _cartItems.value?.let { items ->
            val counts = countItemsByCategory(items)
            _categoryCounts.value = counts
        }
    }

    fun updateTotalPrice(newTotal: Double) {
        _totalPrice.value = newTotal
    }

    fun calculateTotalPrice(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { it.quantity * it.product.price }
        updateTotalPrice(total)
    }



}