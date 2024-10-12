package com.vdsl.myapplication.Service


import com.vdsl.myapplication.Class.User
import com.vdsl.myapplication.Class.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.nio.file.attribute.UserPrincipalLookupService

interface AuthApi {

    @POST("users/reg")
    fun registerUser(@Body user: User): Call<User>

    @POST("users/login")
    fun loginUser(@Body user: User): Call<User>

    @GET("users/validate-token")
    fun validateToken(@Header("Authorization") token: String): Call<Response<Unit>>

    @PUT("users/update/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body user: User,
        @Header("Authorization") token: String
    ): Response<User>

    @GET("users/{id}")
    fun getUser(@Path("id") userId: String?, @Header("Authorization") token: String?): Call<UserResponse>

}