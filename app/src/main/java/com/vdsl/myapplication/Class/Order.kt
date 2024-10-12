package com.vdsl.myapplication.Class

data class OrderRequest(
    val address: String,
    val phoneNumber: String
)

data class CheckoutResponse(
    val msg: String,
    val order: Order
)

data class Order(
    val _id:String,
    val user: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val shippingAddress: ShippingAddress,
    val status: String,
    val createdAt: String
)

data class OrderItem(
    val product: Product,
    val quantity: Int
)


data class ShippingAddress(
    val address: String,
    val phoneNumber: String
)
