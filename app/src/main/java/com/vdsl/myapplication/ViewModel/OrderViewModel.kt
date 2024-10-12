package com.vdsl.myapplication.ViewModel

import android.net.http.HttpException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vdsl.myapplication.Class.CheckoutResponse
import com.vdsl.myapplication.Class.Order
import com.vdsl.myapplication.Class.OrderRequest
import com.vdsl.myapplication.Client.RetrofitClient
import com.vdsl.myapplication.Service.CartService
import com.vdsl.myapplication.Service.OrderService
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val orderService = RetrofitClient.instance.create(OrderService::class.java)
    var orders by mutableStateOf<List<Order>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun checkout(token: String?, address: String, phoneNumber: String, onSuccess: (CheckoutResponse) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = orderService.checkout(
                    token = "Bearer $token",
                    orderRequest = OrderRequest(address, phoneNumber)
                )
                onSuccess(response)
            } catch (e: Exception) {
                onError("Checkout failed: ${e.localizedMessage}")
            }
        }
    }
    fun getOrders(token: String?) {
        viewModelScope.launch {
            if (token.isNullOrBlank()) {
                errorMessage = "Token không hợp lệ. Vui lòng đăng nhập lại."
                return@launch
            }
            isLoading = true
            try {
                val response = orderService.getOrders("Bearer $token")
                orders = response
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Lỗi khi lấy danh sách đơn hàng: ${e.message}"
                isLoading = false
            }
        }
    }
    fun getOrderById(orderId: String): Order? {
        return orders.find { it._id == orderId }
    }
}
