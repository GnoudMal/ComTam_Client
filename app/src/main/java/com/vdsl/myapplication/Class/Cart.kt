package com.vdsl.myapplication.Class

data class AddToCartRequest(
    val productId: String,
    val quantity: Int
)

data class AddToCartResponse(
    val msg: String
)
data class UpdateCartRequest(
    val productId: String,
    val quantity: Int
)

data class UpdateCartResponse(
    val msg: String,
    val updatedCart: List<CartItem>
)
