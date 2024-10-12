package com.vdsl.myapplication.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vdsl.myapplication.Class.Order
import com.vdsl.myapplication.ViewModel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun HistoryScreen(viewModel: OrderViewModel,token: String?,navController: NavController) {
    val orders = viewModel.orders
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(Unit) {
        if (token != null) {
            viewModel.getOrders(token)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF252121))
    ) {
//        topHeader()
        Text(
            text = "Lịch sử",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        if (isLoading) {
            Text("Đang tải dữ liệu...", color = Color.White)
        }

        if (errorMessage != null) {
            Text(text = errorMessage, color = Color.Red)
        }

        // Danh sách các đơn hàng
        if (orders.isEmpty()) {
            Text("Không có đơn hàng nào", color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(orders) { order ->
                    OrderItem(order = order, navController = navController)
                }
            }
        }

        // Thanh điều hướng dưới cùng
    }
}

@Composable
fun OrderItem(order: Order, navController: NavController) {
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = isoFormat.parse(order.createdAt)

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val formattedDate = dateFormat.format(date)
    val formattedTime = timeFormat.format(date)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF3A3A3A), RoundedCornerShape(10.dp))
            .padding(16.dp)
            .clickable {
                navController.navigate("bill_detail/${order._id}")
            }
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = order.status,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$formattedDate   $formattedTime",
                color = Color.White
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${order.items.size} món",
                color = Color.White
            ) // Sửa để hiển thị số lượng món
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${order.totalAmount} VND", color = Color.White)
        }
    }
}

@Composable
fun BillScreen(orderId: String, viewModel: OrderViewModel) {
    
    val order = viewModel.getOrderById(orderId)
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = isoFormat.parse(order?.createdAt ?: "")

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val formattedDate = dateFormat.format(date)
    val formattedTime = timeFormat.format(date)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
            .background(Color(0xFF252121)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tiêu đề hóa đơn
        Text(
            text = "Bi Suon Cha",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Tòa nhà FPT Polytechnic, đường Trịnh Văn Bô, Phương Canh, Nam Từ Liêm, Hà Nội",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = "ĐT: 1900070623 - 0866570613",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Hóa Đơn Bán Hàng",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Ngày: $formattedDate",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Giờ vào: $formattedTime",
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))


        // Tạo bảng cho danh sách món ăn
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 8.dp)
                .background(Color.White)
                .clip(RoundedCornerShape(4.dp))
        ) {
            // Tiêu đề cột
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mặt hàng", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("SL", modifier = Modifier.width(70.dp), fontWeight = FontWeight.Bold)
                Text("Giá", modifier = Modifier.width(70.dp), fontWeight = FontWeight.Bold)
                Text("Thành tiền", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }

            HorizontalDivider()

            LazyColumn(

            ) {
                items(order?.items ?: emptyList()) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(text = "${item.product.name}", modifier = Modifier.weight(1f))
                        Text(text = "${item.quantity}", modifier = Modifier.width(70.dp))
                        Text(text = "${item.product.price} VND", modifier = Modifier.width(70.dp))
                        Text(text = "${item.product.price * item.quantity} VND", modifier = Modifier.weight(1f))
                    }
                    HorizontalDivider()
                }
            }

            HorizontalDivider()

            // Tổng tiền
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tổng:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "${order?.totalAmount} VND", fontWeight = FontWeight.Bold,fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.padding(20.dp))


            Column {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Địa Chỉ Nhận:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(text = "${order?.shippingAddress?.address}", fontWeight = FontWeight.Bold,)
                }

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "SDT:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(text = "${order?.shippingAddress?.phoneNumber}", fontWeight = FontWeight.Bold,)
                }
            }

        }
    }
}







