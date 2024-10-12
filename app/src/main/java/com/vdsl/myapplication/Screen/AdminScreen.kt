package com.vdsl.myapplication.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vdsl.myapplication.R

class AdminScreen : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                    SingleDropdownScreen()

        }
    }
}


@Composable
fun CardProduct(){
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.
        padding(10.dp),
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.mipmap.item_category2), contentScale = ContentScale.Crop, contentDescription = "cardprd", modifier = Modifier.clip(
                CircleShape) )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Com Tam", fontSize = 36.sp, color = Color.Blue, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Mon chinh",fontSize = 20.sp,color = Color.Red, fontStyle = FontStyle.Italic, fontWeight = FontWeight.W600)
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { /*TODO*/ },
                Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),) {
                Text(text = "Bam vao di",fontSize = 18.sp)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleDropdownScreen(){
    val dishName = listOf("Mon Chinh","Mon Phu","Do them","Khac")
    val selectedText = remember { mutableStateOf(dishName[0]) }
    val expanded = remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ){
        ExposedDropdownMenuBox(expanded = expanded.value , onExpandedChange ={
            expanded.value = !expanded.value
        } ) {
            TextField(value = selectedText.value ,
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false}) {
                dishName.forEach{
                    DropdownMenuItem(text = { Text(text = it) },
                        onClick = { selectedText.value = it
                        expanded.value = false})
                }
            }
        }
    }
}

@Preview
@Composable
fun previewCard(){
    CardProduct()
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddDishScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF252121))
//            .padding(16.dp)
//    ) {
//        TopAppBar(
//            title = {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(
//                        painter = painterResource(id = R.mipmap.logo_app),
//                        contentDescription = "Logo",
//                        modifier = Modifier.size(40.dp)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Cum tứm đim",
//                        color = Color.White,
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            },
//            navigationIcon = {
//                IconButton(onClick = { /* Handle back action */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic),
//                        contentDescription = "Back",
//                        tint = Color.White
//                    )
//                }
//            },
//            backgroundColor = Color(0xFF252121),
//            elevation = 0.dp
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Add Image Box
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp))
//                .clickable { /* Handle add image */ },
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_add_image),
//                contentDescription = "Add Image",
//                tint = Color.Gray,
//                modifier = Modifier.size(48.dp)
//            )
//            Text(
//                text = "Thêm hình ảnh",
//                color = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Dropdowns for food type, category and price
//        CustomDropdown(label = "Loại Món", options = listOf("Món chính", "Món phụ"))
//        CustomDropdown(label = "Loại món", options = listOf("Sườn / Sườn mỡ", "Thịt bò", "Gà"))
//        CustomDropdown(label = "Giá", options = listOf("5K-100K", "100K-200K"))
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Add Button
//        Button(
//            onClick = { /* Handle add action */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = Color(0xFFFFC107),
//                contentColor = Color.White
//            )
//        ) {
//            Text(text = "Thêm")
//        }
//    }
//}
//
//@Composable
//fun CustomDropdown(label: String, options: List<String>) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedOption by remember { mutableStateOf(options[0]) }
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(text = label, color = Color.White)
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF393939), shape = RoundedCornerShape(8.dp))
//                .clickable { expanded = true }
//                .padding(8.dp)
//        ) {
//            Text(text = selectedOption, color = Color.White)
//            Icon(
//                painter = painterResource(id = R.drawable.ic_dropdown),
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.align(Alignment.CenterEnd)
//            )
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(onClick = {
//                    selectedOption = option
//                    expanded = false
//                }) {
//                    Text(text = option)
//                }
//            }
//        }
//    }
//    Spacer(modifier = Modifier.height(16.dp))
//}
