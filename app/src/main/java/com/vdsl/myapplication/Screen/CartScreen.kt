package com.vdsl.myapplication.Screen

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.vdsl.myapplication.Class.CartItem
import com.vdsl.myapplication.R
import com.vdsl.myapplication.ViewModel.CartViewModel
import com.vdsl.myapplication.ViewModel.OrderViewModel
import kotlinx.coroutines.launch

@Composable
fun CartScreen(viewModel: CartViewModel, token: String?, navController: NavController) {
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.getCart(token = token, onSuccess = { items ->
                cartItems = items
                isLoading = false
            }, onError = { error ->
                Log.e("CartScreen", "Error fetching cart: $error")
                isLoading = false
            })
        }
    }

    val totalQuantity = cartItems.sumOf { it.quantity }
//    val nowPrice = cartItems.sumOf { it.product.price * it.quantity }
    val categoryCounts by viewModel.categoryCounts.observeAsState(emptyMap())

    val mainDishCount = categoryCounts.getOrDefault("Món Ăn", 0)
    val additionalDishCount = categoryCounts.getOrDefault("Đồ ăn thêm", 0)
    val toppingCount = categoryCounts.getOrDefault("Topping", 0)
    val otherCount = categoryCounts.getOrDefault("Khác", 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF252121))
            .padding(16.dp)
    ) {
        Text(
            text = "Giỏ hàng",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 32.dp)
        )

        if (isLoading) {
            CircularProgressIndicator()
            println("check object cart: " + cartItems)
        } else {
            println("check object cart 2: " + cartItems.toString())
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItem(cartItem = cartItem, token = token, cartViewModel = viewModel)
                    }
                }
            }
        }
//        print("day là gia cu" + nowPrice + " ")
        val totalPrice by viewModel.totalPrice.observeAsState(0.0)
        print("nenne" + totalPrice)
//        Spacer(modifier = Modifier.weight(1f))
        SummarySection(
            mainDishCount = mainDishCount,
            additionalDishCount = additionalDishCount,
            toppingCount = toppingCount,
            otherCount = otherCount,
            totalPrice = totalPrice.toString()
        )

        BottomButtons(navController = navController, token = token, totalPrice = totalPrice)
    }
}

fun countItemsByCategory(cartItems: List<CartItem>): Map<String, Int> {
    val categoryCount = mutableMapOf<String, Int>()

    cartItems.forEach { cartItem ->
        val category = cartItem.product.category
        val quantity = cartItem.quantity

        categoryCount[category] = (categoryCount[category] ?: 0) + quantity
    }

    return categoryCount
}


data class Dish(val name: String, val price: String, val imageRes: Int)

val mainDishes = listOf(
    Dish("Sườn chả", "28K", R.mipmap.food_image),
    Dish("Bì chả", "25K", R.mipmap.food_image)
)

val additionalDishes = listOf(
    Dish("Sườn", "10K", R.mipmap.food_image),
    Dish("Sườn cây", "10K", R.mipmap.food_image),
    Dish("Trứng", "5K", R.mipmap.food_image),
    Dish("Chả", "7K", R.mipmap.food_image)
)

val toppings = listOf(
    Dish("Mỡ hành", "Free", R.mipmap.food_image),
    Dish("Tóp mỡ", "Free", R.mipmap.food_image)
)

val others = listOf(
    Dish("Khăn lạnh", "2K", R.mipmap.food_image),
    Dish("Nước mắm", "Free", R.mipmap.food_image),
    Dish("Nước tương", "Free", R.mipmap.food_image)
)

@Composable
fun SummarySection(
    mainDishCount: Int,
    additionalDishCount: Int,
    toppingCount: Int,
    otherCount: Int,
    totalPrice: String
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        TextSummaryRow("Số lượng món chính", mainDishCount)
        TextSummaryRow("Số lượng món thêm", additionalDishCount)
        TextSummaryRow("Số lượng topping", toppingCount)
        TextSummaryRow("Số lượng món khác", otherCount)
        TextSummaryRow("Tổng số lượng", mainDishCount + additionalDishCount + toppingCount + otherCount)

        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "Tổng tiền: $totalPrice",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun TextSummaryRow(label: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 16.sp)
        Text(text = "$count", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}


@Composable
fun CartItem(cartItem: CartItem, token: String?, cartViewModel: CartViewModel) {
    var quantity by remember { mutableStateOf(cartItem.quantity) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF353535), shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cartItem.product.image_url,
            contentDescription = cartItem.product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = cartItem.product.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
            Text(text = "${quantity} x ${cartItem.product.price}", color = Color(0xFFFF9800), fontSize = 14.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (quantity > 1) {
                    cartViewModel.updateCartItem(token, cartItem, quantity - 1,
                        onSuccess = {
                            quantity -= 1
                            println("Cập nhật số lượng thành công")
                        },
                        onError = { errorMessage ->
                            println("Lỗi: $errorMessage")
                        }
                    )
                }
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_minus), contentDescription = null, tint = Color.White )
            }
            Text(text = "$quantity", color = Color.White, fontSize = 14.sp)
            IconButton(onClick = {
                cartViewModel.updateCartItem(
                    token,
                    cartItem,
                    quantity + 1,
                    onSuccess = {
                        quantity += 1
                    },
                    onError = { errorMessage -> /* Xử lý lỗi */ }
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    }
}


@Composable
fun BottomButtons(navController: NavController,token: String?,totalPrice: Double) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {navController.navigate("payment_detail_screen/$token/$totalPrice") },
            modifier = Modifier
                .width(135.dp)
                .height(48.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
        ) {
            Text(text = "Thanh Toán", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {    },
            modifier = Modifier
                .width(120.dp)
                .height(48.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5))
        ) {
            Text(text = "Reset", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CheckOutScreen(token: String?, totalPrice: Double,viewModel: OrderViewModel) {
    var customerName by remember { mutableStateOf("Chưa nhập tên") }
    var customerPhone by remember { mutableStateOf("Chưa nhập sdt") }
    var customerAddress by remember { mutableStateOf("Chưa nhập địa chỉ") }
    var newPrice by remember { mutableStateOf(totalPrice) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp,),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Thanh Toán", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(10.dp))

        InfoCustomer(
            onInfoChanged = { name, phone, address ->
                customerName = name
                customerPhone = phone
                customerAddress = address
            }
        )
        Spacer(modifier = Modifier.padding(10.dp))
        checkOutMethod()
        Spacer(modifier = Modifier.weight(1f))

        TotalCash(token, totalPrice = newPrice, customerName, customerPhone, customerAddress,viewModel,
            onPaymentSuccess = {
                newPrice = 0.0
            }
        )
    }
}


@Composable
fun TotalCash(
    token: String?,
    totalPrice: Double,
    name: String,
    phone: String,
    address: String,
    viewModel: OrderViewModel,
    onPaymentSuccess: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Card {
        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Tổng Tiền: $totalPrice VND", fontSize = 16.sp)
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { showDialog = true }, modifier = Modifier
                .width(135.dp)
                .height(48.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) {
                Text(text = "Thanh Toán",color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Xác nhận thanh toán") },
            text = { Text(text = "Bạn có muốn thanh toán hay tiếp tục mua sắm?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    isLoading = true
                    viewModel.checkout(token, address, phone,
                        onSuccess = { response ->
                            isLoading = false
                            showSuccessDialog = true
                            Log.d("Payment", "Payment successful: $response") // Log cho việc thanh toán thành công
                            onPaymentSuccess()
                        },
                        onError = { error ->
                            isLoading = false
                            errorMessage = error
                            Log.e("PaymentError", "Error during payment: $error") // Log cho lỗi
                        }
                    )
                }) {
                    Text(text = "Thanh toán")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                }) {
                    Text(text = "Mua tiếp")
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(text = "Thanh Toán Thành Công") },
            text = { Text("Cảm ơn bạn đã thanh toán!") },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
}

@Composable
fun checkOutMethod() {
    var selectedMethod by remember { mutableStateOf("PayPal") }
    Column {
        DetailMethod(
            imageRes = R.mipmap.method2,
            nameMethod = "PayPal",
            selectedMethod = selectedMethod,
            backgroundColor = Color(0xFF3B7DFF),
            onSelect = { selectedMethod = "PayPal" }
        )
        DetailMethod(
            imageRes = R.mipmap.method1,
            nameMethod = "Credit Card",
            selectedMethod = selectedMethod,
            backgroundColor = Color(0xFFE63946),
            onSelect = { selectedMethod = "Credit Card" }
        )
        DetailMethod(
            imageRes = R.mipmap.method3,
            nameMethod = "Thanh Toán Trực Tiếp",
            selectedMethod = selectedMethod,
            backgroundColor = Color(0xFF2A9D8F),
            onSelect = { selectedMethod = "Thanh Toán Trực Tiếp" }
        )
        DetailMethod(
            imageRes = R.mipmap.method4,
            nameMethod = "Momo",
            selectedMethod = selectedMethod,
            backgroundColor = Color(0xFFFFC107),
            onSelect = { selectedMethod = "Momo" }
        )
    }
}

@Composable
fun DetailMethod(
    imageRes: Int,
    nameMethod: String,
    selectedMethod: String,
    backgroundColor: Color,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 7.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Method Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = nameMethod,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = selectedMethod == nameMethod,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.White,
                    unselectedColor = Color.Black
                )
            )
        }
    }
}


@Composable
fun InfoCustomer(onInfoChanged: (String, String, String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var tempName by remember { mutableStateOf(name) }
    var tempPhoneNumber by remember { mutableStateOf(phoneNumber) }
    var tempAddress by remember { mutableStateOf(address) }

    Card(
        modifier = Modifier.clickable {
            tempName = name
            tempPhoneNumber = phoneNumber
            tempAddress = address
            showDialog = true
        }
    ) {
        Text(
            text = "Thông Tin Khách hàng",
            Modifier
                .padding(horizontal = 12.dp)
                .padding(vertical = 5.dp)
        )
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.padding(1.dp))
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = if (name.isEmpty()) "Chưa có tên" else name)
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = if (phoneNumber.isEmpty()) "Chưa có số điện thoại" else phoneNumber)
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = if (address.isEmpty()) "Chưa có địa chỉ" else address)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Nhập Thông Tin Khách Hàng") },
            text = {
                Column {
                    TextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Tên") },
                        placeholder = { Text("Nhập tên khách hàng") }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        value = tempPhoneNumber,
                        onValueChange = { tempPhoneNumber = it },
                        label = { Text("Số Điện Thoại") },
                        placeholder = { Text("Nhập số điện thoại") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        value = tempAddress,
                        onValueChange = { tempAddress = it },
                        label = { Text("Địa Chỉ") },
                        placeholder = { Text("Nhập địa chỉ") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    name = tempName
                    phoneNumber = tempPhoneNumber
                    address = tempAddress
                    showDialog = false

                    onInfoChanged(name, phoneNumber, address)
                }) {
                    Text("Xác Nhận")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}




//@Preview
//@Composable
//fun previewCheckOut(){
//    CheckOutScreen()
//}

