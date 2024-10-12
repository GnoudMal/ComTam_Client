package com.vdsl.myapplication.Screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // Dùng cho các bố cục
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.* // Dùng cho Material3
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.vdsl.myapplication.Class.Product
import com.vdsl.myapplication.Client.RetrofitClient
import com.vdsl.myapplication.R
import com.vdsl.myapplication.ViewModel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: Product,navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    var quantity by remember { mutableStateOf(1) }  // Assuming quantity is managed in the UI
    val sharedPreferences = navController.context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", null)

//    print("check detail 1: " + product)
    val baseUrl = RetrofitClient.BASE_URL
    val imageUrl = if (product.image_url.startsWith("http")) {
        product.image_url // Nếu đã có http thì sử dụng trực tiếp
    } else {
        "$baseUrl${product.image_url}" // Nếu không có, kết hợp với baseUrl
    }


    var message by remember { mutableStateOf<String?>(null) }
    var showMessageDialog by remember { mutableStateOf(false) }
//    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Nền tối
            .padding(horizontal = 16.dp)
    ) {

        // Thanh tiêu đề với biểu tượng quay lại và yêu thích
        TopAppBar(
            title = { Text(product.name, color = Color.White, fontSize = 20.sp) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent // Use this to set the background color
            ),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp()  }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = { /* Xử lý yêu thích */ }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
                }
            }
        )


        // Hình ảnh sản phẩm
        AsyncImage(
            model = imageUrl,
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp)),
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Thông tin sản phẩm
        Text(
            text = product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.category,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Đánh giá, Giao hàng miễn phí, Thời gian
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
            Text(text = "4.7", color = Color.White)

            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
            Text(text = "Free", color = Color.White)

            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
            Text(text = "20 min", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mô tả sản phẩm
        Text(
            text = product.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Tùy chọn kích thước
        Text(text = "SIZE", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SizeOption(size = "10\"", selected = false)
            SizeOption(size = "14\"", selected = true)  // Kích thước được chọn
            SizeOption(size = "16\"", selected = false)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Phần thêm vào giỏ hàng
        AddToCartSection(
            price = product.price.toString(),
            quantity = quantity,
            onIncrease = { quantity++ },
            onDecrease = { if (quantity > 1) quantity-- },
            onAddToCart = {
                cartViewModel.addToCart(
                    token = token,
                    productId = product._id,
                    quantity = quantity,
                    onSuccess = {
                        message = it
                        showMessageDialog = true
                    },
                    onError = {
                        message = it
                        showMessageDialog = true
                    }
                )
            }
        )
    }
    if (showMessageDialog) {
        AlertDialog(
            onDismissRequest = { showMessageDialog = false },
            title = { Text(text = if (message != null) "Message" else "Error") },
            text = { Text(message ?: "Something went wrong.") },
            confirmButton = {
                Button(onClick = { showMessageDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun SizeOption(size: String, selected: Boolean) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(if (selected) Color(0xFFFFC107) else Color(0xFF424242))
            .clickable { /* Xử lý chọn kích thước */ },
        contentAlignment = Alignment.Center
    ) {
        Text(text = size, color = if (selected) Color.Black else Color.White)
    }
}

@Composable
fun AddToCartSection(
    price: String,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onAddToCart: () -> Unit
) {
    val priceValue = price.toDoubleOrNull() ?: 0.0
    val displayPrice = String.format("%.0f", priceValue)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "₫$displayPrice",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onDecrease() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus),
                        contentDescription = "Decrease quantity",
                        tint = Color.White
                    )
                }

                Text(
                    text = quantity.toString(),
                    fontSize = 20.sp,
                    color = Color.White
                )

                IconButton(onClick = { onIncrease() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "Increase quantity",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAddToCart() },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Text(
                text = "Thêm Vào Giỏ Hàng",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}



//@Preview
//@Composable
//fun DetailPreview(){
//    ProductDetailScreen(foodName = "Com", price = 20.1.toString())
//}
