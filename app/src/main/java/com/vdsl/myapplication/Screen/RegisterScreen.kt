package com.vdsl.myapplication.Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vdsl.myapplication.R
import com.vdsl.myapplication.ViewModel.AuthViewModel

//class RegisterScreen : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            RegisterView()
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(navController: NavController,viewModel: AuthViewModel) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var retypePassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var retypePasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0F14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.mipmap.logo_app), // Thay bằng logo của bạn
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Welcome text
        Text(text = "Welcome to SBC !!", color = Color.White, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Register to Continue", color = Color.Gray, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("userName", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth().background(Color.Transparent),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth().background(Color.Transparent),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth().background(Color.Transparent),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password field with visibility toggle
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth().background(Color.Transparent),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.baseline_visibility_24) // Thay bằng biểu tượng hiển thị
                else
                    painterResource(id = R.drawable.baseline_visibility_off_24) // Thay bằng biểu tượng ẩn

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null, tint = Color.Gray)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Re-type Password field with visibility toggle
        OutlinedTextField(
            value = retypePassword,
            onValueChange = { retypePassword = it },
            label = { Text("Re-type Password", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth().background(Color.Transparent),
            visualTransformation = if (retypePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (retypePasswordVisible)
                    painterResource(id = R.drawable.baseline_visibility_24) // Thay bằng biểu tượng hiển thị
                else
                    painterResource(id = R.drawable.baseline_visibility_off_24) // Thay bằng biểu tượng ẩn

                IconButton(onClick = { retypePasswordVisible = !retypePasswordVisible }) {
                    Icon(painter = image, contentDescription = null, tint = Color.Gray)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Register button
        Button(
            onClick = {
                if (password == retypePassword) {
                    viewModel.registerUser(name, userName, email, password,
                        onClearText = {
                            name = ""
                            userName = ""
                            email = ""
                            password = ""
                        },
                        navController = navController)
                } else {
                    Toast.makeText(context, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD17842))
        ) {
            Text(text = "Register", color = Color.White)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Sign in option
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "You have an account? Click ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            TextButton(onClick = { navController.navigate("login") }) {
                Text(text = "Sign in", color = Color(0xFFD17842), fontSize = 14.sp)
            }
        }
    }
}
