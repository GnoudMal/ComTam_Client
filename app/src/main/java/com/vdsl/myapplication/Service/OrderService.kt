package com.vdsl.myapplication.Service

import com.vdsl.myapplication.Class.CheckoutResponse
import com.vdsl.myapplication.Class.Order
import com.vdsl.myapplication.Class.OrderRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderService {
    @POST("products/checkout")
    suspend fun checkout(
        @Header("Authorization") token: String,
        @Body orderRequest: OrderRequest
    ): CheckoutResponse

    @GET("products/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String
    ): List<Order>
}