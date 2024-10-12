package com.vdsl.myapplication.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.* // Dùng cho các bố cục
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.vdsl.myapplication.Class.Product
import com.vdsl.myapplication.Client.RetrofitClient
import com.vdsl.myapplication.R
import com.vdsl.myapplication.ViewModel.ProductViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


//@Composable
//fun HomeScreen() {
//
//    val navController = rememberNavController()
//    val productViewModel: ProductViewModel = viewModel()
//    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
//    val currentRoute = currentBackStackEntry?.destination?.route
//
//    val showBottomBar = when (currentRoute) {
//        "detail/{foodName}/{price}" -> false
//        else -> true
//    }
//    NavHost(navController = navController, startDestination = "home") {
//        composable("home") {
//            FullPizzaAppScreen(navController = navController, viewModel = productViewModel)
//        }
//
//
//        composable(
//            route = Screen.ProductDetail.route,
//            arguments = listOf(
//                navArgument("foodName") { type = NavType.StringType },
//                navArgument("price") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
//            val price = backStackEntry.arguments?.getString("price") ?: ""
//            ProductDetailScreen(foodName = foodName, price = price)
//        }
//    }
//    print("check log nav ne " + currentRoute)
//    print("check log nav boolean " + showBottomBar)
//    if (showBottomBar) {
//        BottomNavigationMenu(navController)
//    }
//}



//class HomeScreen : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        setContent {
//            FullPizzaAppScreen()
//        }
//    }
//}

@Composable
fun FullPizzaAppScreen(navController: NavController, viewModel: ProductViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF252121))
    ) {
        var query by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf("Tất cả") }

        // List of food items
//        val foodItems = listOf(
//            "Sườn" to "25K",
//            "Sườn bì" to "28K",
//            "Sườn chả" to "28K",
//            "Sườn trứng" to "30K",
//            "Chà trứng" to "25K",
//            "Bì trứng" to "25K",
//            "Sườn bì chả" to "35K",
//            "Sườn chà trứng" to "35K",
//            "Sườn bì trứng" to "35K",
//            "Sườn cầy" to "35K",
//            "Bì chả" to "25K"
//        )

        val products = viewModel.products.value

        println("check product 2" + viewModel.products)

        val offers = listOf(
            Offer("Cơm Tấm Special", "Giảm 20%", "Giao hàng miễn phí", R.mipmap.bg_home1),
            Offer("Cơm Tấm Hải Sản", "Giảm 25%", "Giao hàng nhanh", R.mipmap.bg_home1),
            Offer("Cơm Tấm Siêu Cấp", "Mua 1 tặng 1", "Siêu tốc", R.mipmap.bg_home1)
        )

        val filteredItems = products.filter {
            (selectedCategory == "Tất cả") || (it.category == selectedCategory)
        }.filter {
            it.name.contains(query, ignoreCase = true)
        }

        TopHeader()
        SearchBar(query = query, onQueryChanged = { query = it })
        BannerList(offers = offers)
        CategorySection { selectedCategory = it }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(vertical = 15.dp)
        ) {
            items(filteredItems) { product ->
                FoodItemRow(navController, product)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}





@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
//            .padding(bottom = 10.dp),

        verticalAlignment = Alignment.CenterVertically,

    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo_app),
            contentDescription = "Logo",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(end = 10.dp))
        Text(
            text = "Bi Suon Cha",
            fontSize = 20.sp,
            color = Color.White,

            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PizzaBanner() {
    Box(
        modifier = Modifier
            .background(color = Color(0xff312F2E))
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 1.dp)

    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            OfferCard(
                title = "Make Your First Order Here",
                discount = "80% Off",
                deliveryText = "Free delivery",
                imageRes = R.mipmap.bg_home1 // Replace with your image resource
            )
            Spacer(modifier = Modifier.width(8.dp))
            OfferCard(
                title = "50% Chin",
                discount = "",
                deliveryText = "",
                imageRes = R.mipmap.food_image2
            )
        }

    }
}

@Composable
fun BannerList(offers: List<Offer>) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        offers.forEachIndexed { index, offer ->
            OfferCard(
                title = offer.title,
                discount = offer.discount,
                deliveryText = offer.deliveryText,
                imageRes = offer.imageRes,
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(
                        start = if (index == 0) 0.dp else 8.dp,
                        end = 8.dp
                    )
            )
        }
    }
}

@Composable
fun OfferCard(
    title: String,
    discount: String,
    deliveryText: String,
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Hộp chứa nội dung
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = discount, color = Color.White, fontSize = 22.sp)
            }
        }

        // Hộp cho "deliveryText" được căn ở góc dưới bên phải
        if (deliveryText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Red, shape = RoundedCornerShape(2.dp))
                    .padding(6.dp)
            ) {
                Text(text = deliveryText, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

data class Offer(
    val title: String,
    val discount: String,
    val deliveryText: String,
    val imageRes: Int
)



@Composable
fun CategorySection(onCategorySelected: (String) -> Unit) {
    Text(
        text = "Popular Food And Restaurants",
        fontSize = 20.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CategoryItem(name = "Món Ăn", imageId = R.mipmap.item_sp, onClick = { onCategorySelected("Món Ăn") })
        CategoryItem(name = "Đồ ăn thêm", imageId = R.mipmap.item_category2, onClick = { onCategorySelected("Đồ ăn thêm") })
        CategoryItem(name = "Topping", imageId = R.mipmap.item_category3, onClick = { onCategorySelected("Topping") })
        CategoryItem(name = "Khác", imageId = R.mipmap.item_category4, onClick = { onCategorySelected("Khác") })
    }
}

@Composable
fun CategoryItem(name: String, imageId: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Text(text = name, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = imageId),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Tìm kiếm món ăn...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
        singleLine = true
    )
}

//@Composable
//fun SearchableFoodItemList() {
//    var query by remember { mutableStateOf("") }
//
//    // List of food items
//    val foodItems = listOf(
//        "Sườn" to "25K",
//        "Sườn bì" to "28K",
//        "Sườn chả" to "28K",
//        "Sườn trứng" to "30K",
//        "Chà trứng" to "25K",
//        "Bì trứng" to "25K",
//        "Sườn bì chả" to "35K",
//        "Sườn chà trứng" to "35K",
//        "Sườn bì trứng" to "35K",
//        "Sườn cầy" to "35K",
//        "Bì chả" to "25K"
//    )
//
//    // Filter food items based on the search query
//    val filteredItems = foodItems.filter { it.first.contains(query, ignoreCase = true) }
//
//    Column {
//        // Search bar
//        SearchBar(query = query, onQueryChanged = { query = it })
//
//        // Display filtered food items
//        LazyColumn(modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 15.dp)
//            .padding(vertical = 15.dp)) {
//            items(filteredItems) { (name, price) ->
//                FoodItemRow(name, price)
//                Spacer(modifier = Modifier.height(12.dp))
//            }
//        }
//    }
//}

//@Composable
//fun FoodItemList(modifier: Modifier = Modifier) {
//    // Danh sách món ăn mẫu
//    val foodItems = remember {
//        listOf(
//            "Sườn" to "25K",
//            "Sườn bì" to "28K",
//            "Sườn chả" to "28K",
//            "Sườn trứng" to "30K",
//            "Chà trứng" to "25K",
//            "Bì trứng" to "25K",
//            "Sườn bì chả" to "35K",
//            "Sườn chà trứng" to "35K",
//            "Sườn bì trứng" to "35K",
//            "Sườn cầy" to "35K",
//            "Bì chả" to "25K"
//        )
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 15.dp)
//            .padding(vertical = 15.dp)
//    ) {
//        items(foodItems) {(name, price) ->
//            FoodItemRow(name, price)
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//    }
//
//
//}

@Composable
fun FoodItemCard(
    foodName: String,
    rating: Float,
    distance: String,
    deliveryFee: String,
    imageRes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(text = foodName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "$rating ★", fontSize = 14.sp, color = Color.Gray)
            Text(text = distance, fontSize = 14.sp, color = Color.Gray)
        }
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = deliveryFee, color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun FoodItemRow(navController: NavController, product: Product) {
    val gson = Gson()
    val baseUrl = RetrofitClient.BASE_URL
    val imageUrl = if (product.image_url.startsWith("http")) {
        product.image_url // Nếu đã có http thì sử dụng trực tiếp
    } else {
        "$baseUrl${product.image_url}" // Nếu không có, kết hợp với baseUrl
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF2F2D2D))
            .padding(8.dp)
            .clickable {
                val productJson = gson.toJson(product)
                val encodedProductJson = URLEncoder.encode(productJson, StandardCharsets.UTF_8.toString())
                navController.navigate("detail/$encodedProductJson")
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = product.name, fontSize = 18.sp, color = Color.White)
                Text(text = "${product.price} VND", fontSize = 18.sp, color = Color(0xFFFE724C))
            }
        }
    }
}







//@Composable
//fun BottomNavigationMenu(navController: NavController) {
//    var selectedItem by remember { mutableStateOf(0) }
//
//    BottomAppBar(
//        containerColor = Color(0xFF312C2C),
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceAround
//        ) {
//            BottomNavigationItem(
//                icon = R.drawable.ic_home,
//                label = "Trang chủ",
//                isSelected = selectedItem == 0,
//                onClick = {
//                    selectedItem = 0
//                    navController.navigate(Screen.Home.route)
//                }
//            )
//            BottomNavigationItem(
//                icon = R.drawable.ic_history,
//                label = "Lịch sử",
//                isSelected = selectedItem == 1,
//                onClick = {
//                    selectedItem = 1
//                    navController.navigate(Screen.History.route)
//                }
//            )
//            BottomNavigationItem(
//                icon = R.drawable.ic_cart,
//                label = "Giỏ hàng",
//                isSelected = selectedItem == 2,
//                onClick = {
//                    selectedItem = 2
//                    navController.navigate(Screen.Cart.route)
//                }
//            )
//            BottomNavigationItem(
//                icon = R.drawable.ic_profile,
//                label = "Hồ sơ",
//                isSelected = selectedItem == 3,
//                onClick = {
//                    selectedItem = 3
//                    navController.navigate(Screen.Profile.route)
//                }
//            )
//        }
//    }
//}
//
//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = Screen.Home.route) {
//        composable(Screen.Home.route) { HomeScreen() }
//        composable(Screen.History.route) { HistoryScreen() }
//        composable(Screen.Cart.route) { CartScreen() }
//        composable(Screen.Profile.route) { ProfileScreen() }
//    }
//
//    // Đặt BottomNavigationMenu ở dưới cùng
//    BottomNavigationMenu(navController = navController)
//}
//
//
//
//@Composable
//fun BottomNavigationItem(
//    icon: Int,
//    label: String,
//    isSelected: Boolean,
//    onClick: () -> Unit
//) {
//    val color = if (isSelected) Color(0xFFFFB703) else Color.White // Change color when selected
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .clickable(onClick = onClick) // Make the item clickable
//            .padding(8.dp)
//    ) {
//        Icon(
//            painter = painterResource(id = icon),
//            contentDescription = label,
//            tint = color,
//            modifier = Modifier.size(24.dp) // Icon size
//        )
//        Text(
//            text = label,
//            color = color,
//            fontSize = 12.sp
//        )
//    }
//}



@Preview(showBackground = true)
@Composable
fun FullPizzaAppScreenPreview() {
    val navController = rememberNavController()
    FullPizzaAppScreen(navController = navController) // Không cần truyền viewModel ở đây
}


