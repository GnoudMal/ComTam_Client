package com.vdsl.myapplication.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.vdsl.myapplication.Class.User
import com.vdsl.myapplication.Class.UserResponse
import com.vdsl.myapplication.Client.RetrofitClient
import com.vdsl.myapplication.Service.AuthApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel(){
    private val authApi = RetrofitClient.instance.create(AuthApi::class.java)

    fun registerUser(
        name: String,
        username: String,
        email: String,
        password: String,
        onClearText: () -> Unit,
        navController: NavController
    ) {
        val user = User(name = name, username = username, email =  email, password = password)
        authApi.registerUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    println("Đăng ký thành công: ${response.body()}")
                    Log.e("Check Loi Res","Dang ky thanh cong")



                    onClearText()

                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                } else {
                    Log.e("Check Loi Res", "Dang ky That Bai: ${response.message()}")
                    println("Lỗi đăng ký")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println("Lỗi: ${t.message}")
                Log.e("Check Loi Res", " ${t.message}")
            }
        })
    }



    fun loginUser(
        email: String,
        password: String,
        rememberMe: Boolean,
        onClearText: () -> Unit,
        onResult: (Boolean, String) -> Unit,
        navController: NavController
    ) {
        val user = User(email = email, password = password)
        authApi.loginUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    val userId = response.body()?._id
                    print("check token: " + token)
                    print("check userID: " + userId)

                    token?.let {
                        val sharedPreferences = navController.context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("token", it)
                            putString("userId", userId)
                            putBoolean("rememberMe", rememberMe)
                            apply()
                        }
                    }

                    onClearText()
                    onResult(true, "Đăng nhập thành công")

                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    onResult(false, "Sai mật khẩu hoặc tài khoản")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                onResult(false, "Lỗi: ${t.message}")
            }
        })
    }




    fun checkTokenValidity(token: String, onResult: (Boolean) -> Unit, context: Context) {
        authApi.validateToken("Bearer $token").enqueue(object : Callback<Response<Unit>> {
            override fun onResponse(call: Call<Response<Unit>>, response: Response<Response<Unit>>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Response<Unit>>, t: Throwable) {
                onResult(false)
            }
        })
    }

    fun getUser(userId: String?, token: String?, onResult: (User?, String?) -> Unit) {
        val authToken = "Bearer $token"
        if (userId == null || token == null) {
            Log.e("getUser", "userId or token is null")
            onResult(null, "userId or token is null")
            return
        }

        Log.e("getUser", "userId or token is $token")
        Log.e("getUser", "userId or token is $userId")

        authApi.getUser(userId, authToken).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("getUser", "Response Code: ${response.code()}, Message: ${response.message()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        onResult(it.user, null) // Lấy user từ response
                        Log.e("getUser", "Thanh Cong:  ${it.user}")
                    } ?: onResult(null, "Empty response body")
                } else {
                    Log.e("getUser", "Error: ${response.message()}")
                    onResult(null, response.message())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("getUser", "Failed to fetch user: ${t.message}")
                onResult(null, t.message)
            }
        })
    }

    fun updateUser(userId: String, user: User, token: String, onResult: (Boolean, String?) -> Unit) {
        val authToken = "Bearer $token"
        viewModelScope.launch {
            try {
                // Gọi API updateUser
                val response: Response<User> = authApi.updateUser(userId, user, authToken)

                if (response.isSuccessful) {
                    // Thành công, trả về dữ liệu người dùng
                    onResult(true, response.body()?.toString())
                    Log.d("UpdateUser", "Cập nhật thành công: ${response.body()}")
                } else {
                    // Lỗi khi cập nhật
                    Log.e("UpdateUser", "Lỗi: ${response.message()}")
                    onResult(false, response.message())
                }
            } catch (e: Exception) {
                // Xử lý lỗi
                Log.e("UpdateUser", "Exception: ${e.message}")
                onResult(false, e.message)
            }
        }
    }


}