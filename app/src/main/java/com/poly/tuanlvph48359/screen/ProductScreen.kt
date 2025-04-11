package com.poly.tuanlvph48359.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.poly.tuanlvph48359.R
import com.poly.tuanlvph48359.model.FavoriteItem
import com.poly.tuanlvph48359.model.Product
import com.poly.tuanlvph48359.viewmodel.CartViewModel
import com.poly.tuanlvph48359.viewmodel.FavoriteViewModel
import com.poly.tuanlvph48359.viewmodel.ProductViewModel

@Composable
fun ProductScreen(
    navController: NavController,
    productId: Int,
    cartViewModel: CartViewModel,
    favoriteViewModel: FavoriteViewModel,
    userId: String,
    productViewModel: ProductViewModel = viewModel()
) {
    LaunchedEffect(productId) {
        productViewModel.getProductById(productId)
    }

    val productDetailState = productViewModel.productDetailState.value
    val product = productDetailState.product
    val isLoading = productDetailState.isLoading
    val error = productDetailState.error

    var quantity by remember { mutableStateOf(1) }
    var selectedColorIndex by remember { mutableStateOf(0) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        } else if (error.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Đã xảy ra lỗi: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
                    Button(
                        onClick = { productViewModel.getProductById(productId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Thử lại")
                    }
                }
            }
        } else if (product != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp, bottom = 8.dp)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart).size(48.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(24.dp))
                    }
                }

                val context = LocalContext.current
                val imageResId = remember(product.image) {
                    context.resources.getIdentifier(product.image, "drawable", context.packageName)
                }

                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    Image(
                        painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.stand),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp))
                    )

                    Column(
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val colors = listOf(Color.Gray, Color(0xFFAF7D57), Color(0xFFE4C7AF))
                        colors.forEachIndexed { index, color ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (selectedColorIndex == index) 2.dp else 0.dp,
                                        color = if (selectedColorIndex == index) Color.Black else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColorIndex = index }
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(product.name, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$ ${String.format("%.2f", product.price)}",
                            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.size(28.dp).clip(CircleShape).background(Color.LightGray.copy(alpha = 0.3f)).clickable { quantity++ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            }

                            Text(String.format("%02d", quantity), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black))

                            Box(
                                modifier = Modifier.size(28.dp).clip(CircleShape).background(Color.LightGray.copy(alpha = 0.3f)).clickable {
                                    if (quantity > 1) quantity--
                                },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("−", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .clickable { navController.navigate("review") }
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_star_filled), contentDescription = "Rating", tint = Color(0xFFFFB800), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(product.rating.toString(), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("(${product.reviewCount} reviews)", style = TextStyle(fontSize = 14.sp, color = Color.Gray))
                    }

                    Text(product.description, style = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, color = Color.DarkGray))
                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray.copy(alpha = 0.3f))
                                .clickable {
                                    val item = FavoriteItem(
                                        userId = userId,
                                        id = product.id,
                                        name = product.name,
                                        price = product.price,
                                        imageRes = imageResId
                                    )
                                    favoriteViewModel.addToFavorites(item)
                                    Log.d("FAVORITE_DEBUG", "Đã thêm ${item.name} vào danh sách yêu thích")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bookmark),
                                contentDescription = "Bookmark",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Button(
                            onClick = {
                                val productToAdd = Product(
                                    id = product.id,
                                    name = product.name,
                                    price = product.price,
                                    imageRes = imageResId
                                )

                                repeat(quantity) {
                                    cartViewModel.addToCart(productToAdd)
                                }

                                Log.d("CART_DEBUG", "Đã thêm ${product.name} với số lượng $quantity")

                                navController.navigate("cart")
                            },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Add to cart", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White))
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.LightGray.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}