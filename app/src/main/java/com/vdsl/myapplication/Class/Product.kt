package com.vdsl.myapplication.Class

data class Product(
    val _id: String,
    val name: String,
    val image_url: String,
    val price: Double,
    val description: String,
    val category: String
)

data class CartItem(
    val _id: String,
    val product: Product,
    var quantity: Int
)
