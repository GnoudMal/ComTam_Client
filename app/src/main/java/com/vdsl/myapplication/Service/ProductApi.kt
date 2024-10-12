package com.vdsl.myapplication.Service

import com.vdsl.myapplication.Class.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<Product>
}