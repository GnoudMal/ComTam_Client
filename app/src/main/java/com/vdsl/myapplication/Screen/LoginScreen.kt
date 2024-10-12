package com.vdsl.myapplication.Screen

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vdsl.myapplication.R
import com.vdsl.myapplication.ViewModel.AuthViewModel


class LoginScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
         Navigation()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()
    val navController = rememberNavController()

    val sharedPreferences = LocalContext.current.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", null)
    val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
    val userId = sharedPreferences.getString("userId", null)

    if (token != null && rememberMe) {
        viewModel.checkTokenValidity(token, { isValid ->
            if (isValid) {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            }
        }, context)
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginView(navController, viewModel) }
        composable("register") { RegisterView(navController, viewModel) }
        composable("main") { MainScreen(navController) }
        composable(Screen.PersonalDetail.route) { EditProfileScreen(viewModel, token = token, userId = userId) }
        composable(Screen.AddFood.route) { AddDishScreen() }
    }
}

//private suspend fun checkTokenValidity(token: String): Boolean {
//    return try {
//        val response = apiService.checkToken(token) // Thay thế bằng hàm thực tế của bạn
//        response.isSuccessful
//    } catch (e: Exception) {
//        false
//    }
//}

@Composable
fun CustomCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val checkboxColor = if (checked) Color(0xFFD17842) else Color.LightGray
    val iconColor = if (checked) Color.White else Color.Transparent

    Box(
        modifier = Modifier
            .size(24.dp)
            .border(2.dp, checkboxColor, shape = RoundedCornerShape(4.dp))
            .background(
                if (checked) checkboxColor else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavController,viewModel: AuthViewModel) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* Handle if needed */ },
            title = { Text("Thông báo") },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    if (dialogMessage == "Đăng nhập thành công!") {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFDF6E4))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo_app),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop,

        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Welcome to SBC !", color = Color(0xFF6B4226), fontSize = 24.sp)
//        TestDropdownScreen()

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Login to Continue !", color = Color.Gray, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFD17842),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF6B4226),
                focusedLabelColor = Color(0xFF6B4226),
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color(0xFF6B4226),
                unfocusedTextColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.baseline_visibility_off_24)
                else
                    painterResource(id = R.drawable.baseline_visibility_24)

                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(painter = image, contentDescription = null, tint = Color.Gray)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFD17842),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF6B4226),
                focusedLabelColor = Color(0xFF6B4226),
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color(0xFF6B4226),
                unfocusedTextColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomCheckbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it }
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Remember Me", color = Color.Gray, fontSize = 14.sp)
        }



        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.loginUser(
                        email = email,
                        password = password,
                        rememberMe = rememberMe,
                        onClearText = {
                            email = ""
                            password = ""
                        },
                        onResult = { success, message ->
                            if (success) {
                                dialogMessage = "Đăng nhập thành công!"
                                showDialog = true
                            } else {
                                dialogMessage = message
                                showDialog = true
                            }
                        },
                        navController = navController
                    )
                } else {
                    Toast.makeText(
                        context, "Please enter username and password", Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD17842))
        ) {
            Text(text = "Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.weight(1f))
            Text(text = " or ", color = Color.Gray, modifier = Modifier.padding(8.dp))
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {  }) {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_facebook),
                    contentDescription = "Facebook",
                    tint = Color(0xFF6B4226),
                    modifier = Modifier.size(48.dp)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_gmail),
                    contentDescription = "Google",
                    tint = Color(0xFF6B4226),
                    modifier = Modifier.size(100.dp)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.apple_logo_svgrepo_com),
                    contentDescription = "Apple",
                    tint = Color(0xFF6B4226),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Don't have an account?",
                color = Color.Gray,
                fontSize = 14.sp,
            )
            TextButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier.height(40.dp)
            ) {
                Text(text = "Sign up", color = Color(0xffD17842), fontSize = 14.sp, )
            }
        }
        TextButton(
            onClick = {  },
            modifier = Modifier.height(40.dp)
        ) {
            Text(text = "Quên mật khẩu?", color = Color(0xffD17842), fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Cần trợ giúp? Liên hệ chúng tôi qua email support@LTDNe.com",
            color = Color(0xffD17842),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun TestDropdownScreen() {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    CustomDropdown(
        label = "Test Dropdown",
        options = listOf("Option 1", "Option 2", "Option 3"),
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun LoginPreview() {
//    LoginView(rememberNavController())
//}