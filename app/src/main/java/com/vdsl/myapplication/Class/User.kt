package com.vdsl.myapplication.Class

data class User (
    val _id: String? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String?= null,
    val password: String? = null,
    val token: String? = null,
    val phoneNumber: String? = null, // Số điện thoại không cần thiết
    val address: String? = null
)

data class UserResponse(
    val user: User
)

