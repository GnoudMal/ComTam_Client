package com.vdsl.myapplication.Service

import com.vdsl.myapplication.Class.AddToCartRequest
import com.vdsl.myapplication.Class.AddToCartResponse
import com.vdsl.myapplication.Class.CartItem
import com.vdsl.myapplication.Class.UpdateCartRequest
import com.vdsl.myapplication.Class.UpdateCartResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CartService {
    @POST("products/cart")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body addToCartRequest: AddToCartRequest
    ): AddToCartResponse

    @GET("products/get-cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): Response<List<CartItem>>

    @POST("products/update-cart")
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Body updateCartRequest: UpdateCartRequest
    ): Response<CartItem>
}