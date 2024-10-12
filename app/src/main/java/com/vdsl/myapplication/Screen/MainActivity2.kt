package com.vdsl.myapplication.Screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.vdsl.myapplication.Class.Product
import com.vdsl.myapplication.Class.User
import com.vdsl.myapplication.R
import com.vdsl.myapplication.ViewModel.AuthViewModel
import com.vdsl.myapplication.ViewModel.CartViewModel
import com.vdsl.myapplication.ViewModel.OrderViewModel
import com.vdsl.myapplication.ViewModel.ProductViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import androidx.compose.foundation.layout.Row as Row1
import androidx.compose.material3.IconButton as IconButton1
import androidx.compose.material3.Text as Text1

//class MainActivity2 : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            RootComponent()
//        }
//    }
//}

//@Composable
//fun MainActivity() {
//    FullPizzaAppScreen()
//}


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object PersonalDetail : Screen("personalDetail")
    object AddFood : Screen("addFood")
    object ProductDetail : Screen("detail/{productJson}")
    object CheckOut : Screen("payment_detail_screen/{token}/{totalPrice}")
    object BillDetail : Screen("bill_detail/{orderId}")
}


@Composable
fun MainScreen(navMain: NavController) {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel : CartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    val sharedPreferences = navController.context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", null)

    val showBottomBar = when (currentRoute) {
        "detail/{productJson}" -> false
        else -> true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF252121))
    ) {
        // Phần nội dung chính
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    FullPizzaAppScreen(navController = navController, viewModel = productViewModel)
                }
                composable(Screen.History.route) { HistoryScreen(viewModel = orderViewModel,token = token,navController = navController) }
                composable(Screen.Cart.route) { CartScreen(viewModel = cartViewModel,token = token,navController) }
                composable(Screen.Profile.route) { ProfileScreen(navMain) }
                composable(
                    route = Screen.BillDetail.route,
                    arguments = listOf(
                        navArgument("orderId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                    BillScreen(orderId = orderId, viewModel = orderViewModel)
                }

//                composable(Screen.CheckOut.route) { CheckOutScreen() }
                composable(
                    route = Screen.CheckOut.route,
                    arguments = listOf(
                        navArgument("token") { type = NavType.StringType },
                        navArgument("totalPrice") { type = NavType.FloatType }
                    )
                ) { backStackEntry ->
                    val token = backStackEntry.arguments?.getString("token") ?: ""
                    val totalPrice = backStackEntry.arguments?.getFloat("totalPrice")?.toDouble() ?: 0.0 // Chuyển đổi thành Double
                    CheckOutScreen(token = token, totalPrice = totalPrice, viewModel = orderViewModel)
                }
                composable(
                    route = Screen.ProductDetail.route,
                    arguments = listOf(
                        navArgument("productJson") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val productJson = backStackEntry.arguments?.getString("productJson") ?: ""
                    val decodedProductJson = URLDecoder.decode(productJson, StandardCharsets.UTF_8.toString()) // Giải mã JSON
                    val product = Gson().fromJson(decodedProductJson, Product::class.java) // Phân tích cú pháp JSON
                    ProductDetailScreen(product = product,navController) // Truyền đối tượng Product
                }
            }
        }

        if (showBottomBar) {
            BottomNavigationMenu(navController)
        }
    }
}

@Composable
fun BottomNavigationMenu(navController: NavController) {
    var selectedItem by remember { mutableStateOf(Screen.Home.route) }

    BottomAppBar(
        containerColor = Color(0xFF312C2C),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row1(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavigationItem(
                icon = R.drawable.ic_home,
                label = "Trang chủ",
                isSelected = selectedItem == Screen.Home.route,
                onClick = {
                    selectedItem = Screen.Home.route
                    navController.navigate(Screen.Home.route)
                }
            )
            BottomNavigationItem(
                icon = R.drawable.ic_history,
                label = "Lịch sử",
                isSelected = selectedItem == Screen.History.route,
                onClick = {
                    selectedItem = Screen.History.route
                    navController.navigate(Screen.History.route)
                }
            )
            BottomNavigationItem(
                icon = R.drawable.ic_cart,
                label = "Giỏ hàng",
                isSelected = selectedItem == Screen.Cart.route,
                onClick = {
                    selectedItem = Screen.Cart.route
                    navController.navigate(Screen.Cart.route)
                }
            )
            BottomNavigationItem(
                icon = R.drawable.ic_profile,
                label = "Hồ sơ",
                isSelected = selectedItem == Screen.Profile.route,
                onClick = {
                    selectedItem = Screen.Profile.route
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
    }
}






@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF252121))
            .padding(16.dp)
    ) {

        Text1(
            text = "Setting",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )


        SettingItem(iconId = R.drawable.ic_profile, title = "Personal Details") {
            navController.navigate("personalDetail")
        }
        SettingItem(iconId = R.drawable.ic_location, title = "Address")
        SettingItem(iconId = R.drawable.ic_payment, title = "Payment Method")
        SettingItem(iconId = R.drawable.ic_info, title = "About")
        SettingItem(iconId = R.drawable.ic_admin, title = "Admin"){
            navController.navigate("addFood")
        }
        SettingItem(iconId = R.drawable.baseline_help_24, title = "Help")
        SettingItem(
            iconId = R.drawable.baseline_logout_24,
            title = "Log out",
            onClick = {
                showDialog = true
            }
        )
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Xác nhận đăng xuất") },
                text = { Text("Bạn có chắc chắn muốn đăng xuất không?") },
                confirmButton = {
                    Button(
                        onClick = {
                            logOut(context, navController)
                            Toast.makeText(context, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) {
                        Text("Đồng ý")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

fun logOut(context: Context, navController: NavController) {
    val sharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove("token") // Xóa token
    editor.putBoolean("rememberMe", false) // Đặt lại trạng thái "Remember Me"
    editor.apply() // Lưu lại thay đổi

    // Điều hướng về màn hình đăng nhập
    navController.navigate("login") {
        popUpTo("profile") { inclusive = true } // Đảm bảo rằng người dùng không quay lại màn hình chính
    }
}


@Composable
fun SettingItem(iconId: Int, title: String,onClick: () -> Unit = {}) {
    Row1(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row1(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFD1784233), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = title,
                    tint = Color(0xFFD17842),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text1(
                text = title,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "Arrow",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(authViewModel: AuthViewModel, userId: String?, token: String?) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var ward by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var houseNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    Log.e("Check Loi", "Name: " + name)

    LaunchedEffect(userId, token) {
        authViewModel.getUser(userId, token) { user, errorMessage ->
            if (user != null) {
                name = user.name ?: "DTL"
                Log.e("Da O Day","ten la ${user}")
                phone = user.phoneNumber ?: ""
                val addressParts = user.address?.split(" - ")
                if (addressParts?.size == 3) {
                    houseNumber = addressParts[0]
                    street = addressParts[1]
                    ward = addressParts[2]
                }
                isLoading = false
            } else {
//                errorMessage?.let {
//                    this@EditProfileScreen.errorMessage = it
//                }
                Log.e("Da O Day","ALOOO")
                isLoading = false
            }
        }
    }


    if (isLoading) {
        CircularProgressIndicator()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF252121))
            .padding(16.dp)
    ) {
        Text1(
            text = "Sửa hồ sơ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.item_category3),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = "Edit Avatar",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)

                    .background(Color(0xFF252121), CircleShape)
                    .padding(8.dp)
            )
        }


        OutlinedTextField(
            value = name,
            onValueChange = {name = it },
            label = { Text1("Họ và Tên") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFF393939),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        OutlinedTextField(
            value = phone,
            onValueChange = {phone = it },
            label = { Text1("Số điện thoại") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFF393939),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        OutlinedTextField(
            value = ward,
            onValueChange = {ward = it },
            label = { Text1("Phường") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFF393939),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        OutlinedTextField(
            value = street,
            onValueChange = { street = it },
            label = { Text1("Đường") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFF393939),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        OutlinedTextField(
            value = houseNumber,
            onValueChange = { houseNumber = it },
            label = { Text1("Số nhà") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFF393939),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        val context = LocalContext.current

        Button(
            onClick = {
                val updatedUser = User(
                    name = name,
                    phoneNumber = phone,
                    address = "$houseNumber - $street - $ward"
                    // Giữ nguyên email và password, không thêm vào đây
                )

                // Gọi hàm updateUser từ authViewModel
                authViewModel.updateUser(userId ?: "", updatedUser, token ?: "") { success, message ->
                    if (success) {
                        // Cập nhật thành công
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Cập nhật thất bại
                        Toast.makeText(context, "Cập nhật thất bại: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6D42),
                contentColor = Color.White
            )
        ) {
            Text1(text = "Lưu")
        }
    }
}


//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = Screen.Home.route) {
//        composable(Screen.Home.route) { MainActivity() }
//        composable(Screen.History.route) { HistoryScreen() }
//        composable(Screen.Cart.route) { CartScreen() }
//        composable(Screen.Profile.route) { ProfileScreen() }
//    }
//    BottomNavigationMenu(navController = navController)
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topHeader(){
    Box {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        TopAppBar(
            title = {
                Row1(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.mipmap.logo_app),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp) )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text1(
                        text = "Cum tứm đim",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton1(onClick = { /* Handle back action */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF252121)
            ),
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDishScreen() {
    var dishName by remember { mutableStateOf("") }
    var dishPrice by remember { mutableStateOf("") }
    var dishDescription by remember { mutableStateOf("") }
    var selectedMainDish by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF252121))
            .padding(16.dp)
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.mipmap.logo_app),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp) )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bi Suon Cha",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton1(onClick = { /* Handle back navigation */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF252121)
            ),
            scrollBehavior = scrollBehavior
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Add Image Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp))
                .clickable { /* Handle image add */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "Add Image",
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Thêm hình ảnh",
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields for Name, Price, and Description
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {

            // Dish name
            TextField(
                value = dishName,
                onValueChange = { dishName = it },
                label = { Text("Tên món") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dish price
            TextField(
                value = dishPrice,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        dishPrice = newValue
                    }
                },
                label = { Text("Giá (VNĐ)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White
                )
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Dish description
            TextField(
                value = dishDescription,
                onValueChange = { dishDescription = it },
                label = { Text("Mô tả") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown for category
            CustomDropdown(
                label = "Thể Loại",
                options = listOf("Món chính", "Đồ Ăn Thêm","Toping","Khác"),
                selectedOption = selectedMainDish,
                onOptionSelected = { selectedMainDish = it }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Add button
        Button(
            onClick = { /* Handle add action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC107),
                contentColor = Color.White
            )
        ) {
            Text(text = "Thêm")
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selectedOption ?: "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .clickable { expanded = true }
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}






@Composable
fun BottomNavigationItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) Color(0xFFFFB703) else Color.White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text1(
            text = label,
            color = color,
            fontSize = 12.sp
        )
    }
}

//@Preview
//@Composable
//fun DropdownPreview() {
//    HomeScreen()
//}