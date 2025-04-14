package com.poly.tuanlvph48359.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.poly.tuanlvph48359.R
import com.poly.tuanlvph48359.model.Product
import com.poly.tuanlvph48359.model.ProductResponse
import com.poly.tuanlvph48359.viewmodel.CartViewModel
import com.poly.tuanlvph48359.viewmodel.ProductViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel = viewModel()
) {
    val productsState = productViewModel.productsState.value
    val products = productsState.products
    val isLoading = productsState.isLoading
    val error = productsState.error


    val categories = listOf("Popular", "Chair", "Table", "Armchair", "Bed", "Lamp")
    var selectedCategory by remember { mutableStateOf("Popular") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 16.dp)
            ) {
                // Phần tiêu đề - giữ nguyên
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Make home", style = TextStyle(color = Color.Gray, fontSize = 14.sp))
                    Text("BEAUTIFUL", style = TextStyle(color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phần tìm kiếm - thêm mới
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = productViewModel.searchQuery.value,
                        onValueChange = { productViewModel.updateSearchQuery(it) },
                        placeholder = { Text("Tìm kiếm sản phẩm...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.navigate("cart") }
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 0.dp,
                modifier = Modifier.height(56.dp)
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("favorites")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.bookmark),
                            contentDescription = "Bookmarks",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("notification")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.notifications),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("profile")
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "Profile",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
            }
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            CategoriesSection(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    if (category == "Popular") {
                        productViewModel.getAllProducts()
                    } else {
                        productViewModel.getProductsByCategory(category.lowercase())
                    }
                }
            )

            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Black)
                }

                error.isNotEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(error, color = Color.Red, fontSize = 16.sp, modifier = Modifier.padding(16.dp))
                        Button(onClick = { productViewModel.getAllProducts() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                            Text("Thử lại")
                        }
                    }
                }

                else -> {
                    ProductGrid(products = products, navController = navController, cartViewModel = cartViewModel)
                }
            }
        }
    }
}

@Composable
fun ProductGrid(
    products: List<ProductResponse>,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ApiProductItem(
                product = product,
                onClick = { navController.navigate("product/${product.id}") },
                cartViewModel = cartViewModel
            )
        }
    }
}

@Composable
fun ApiProductItem(
    product: ProductResponse,
    onClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(product.image, "drawable", context.packageName)

    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
        ) {
            Image(
                painter = painterResource(id = if (imageResId != 0) imageResId else R.drawable.ic_chair),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x80FFFFFF))
                    .align(Alignment.BottomEnd)
                    .clickable {
                        val cartProduct = Product(
                            id = product.id,
                            name = product.name,
                            price = product.price,
                            imageRes = imageResId
                        )

                        Log.d("CART_DEBUG", "Đã bấm thêm ${product.name} vào giỏ hàng")

                        cartViewModel.addToCart(cartProduct)

                        Log.d("CART_DEBUG", "Số lượng trong cart hiện tại: ${cartViewModel.cartItems.size}")

                        Toast.makeText(context, "${product.name} đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shopping_bag),
                    contentDescription = "Add to cart",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
        )

        Text(
            text = "$ ${String.format("%.2f", product.price)}",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        )
    }
}
// Tìm hàm CategoriesSection trong HomeScreen.kt
@Composable
fun CategoriesSection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Đặt chiều rộng tối đa để chỉ hiển thị đến "Bed"
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            categories.forEach { category ->
                CategoryItem(
                    category = category,
                    isSelected = category == selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean,
    onCategorySelected: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onCategorySelected(category) }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) Color.Black else Color(0xFFF0F0F0))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = when (category) {
                    "Popular" -> painterResource(id = R.drawable.ic_star)
                    "Chair" -> painterResource(id = R.drawable.ic_chair)
                    "Table" -> painterResource(id = R.drawable.ic_table)
                    "Armchair" -> painterResource(id = R.drawable.ic_armchair)
                    "Bed" -> painterResource(id = R.drawable.ic_bed)
                    "Lamp" -> painterResource(id = R.drawable.ic_lamp)
                    else -> painterResource(id = R.drawable.ic_star)
                },
                contentDescription = category,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = category,
            style = TextStyle(
                color = if (isSelected) Color.Black else Color.Gray,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        )
    }
}

