package com.poly.tuanlvph48359.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.poly.tuanlvph48359.viewmodel.CartViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.poly.tuanlvph48359.R
import com.poly.tuanlvph48359.viewmodel.AppViewModel
import com.poly.tuanlvph48359.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val cartViewModel: CartViewModel = viewModel()
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    // Khởi tạo AppViewModel để xử lý đăng xuất
    val appViewModel: AppViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    // Lấy trạng thái từ ViewModel
    val uiState = viewModel.uiState

    // Trạng thái cho dialog đăng xuất
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Dialog xác nhận đăng xuất
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Xác nhận") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?") },
            confirmButton = {
                Button(
                    onClick = {
                        Log.d("LOGOUT_DEBUG", "Đã gọi clearCart() khi đăng xuất. Số lượng còn lại: ${cartViewModel.cartItems.size}")

                        appViewModel.logout {
                            // Sau khi đăng xuất thành công, điều hướng về trang login
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                        showLogoutDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Đăng xuất", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            // Bottom navigation bar
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 0.dp,
                modifier = Modifier.height(56.dp)
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    },
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
                        navController.navigate("favorites") {
                            launchSingleTop = true
                        }
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
                        navController.navigate("notification") {
                            launchSingleTop = true
                        }
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
                    selected = true,
                    onClick = { },
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
        // Hiển thị loading nếu đang tải dữ liệu
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = Color(0xFFF5F5F5) // Light gray background
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // ===== APP BAR =====
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 16.dp)
                    ) {
                        // Search icon (left)
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(24.dp)
                        )

                        // Title centered - "Profile"
                        Text(
                            text = "Profile",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        )

                        // Logout icon (right) - Hiển thị dialog khi nhấn
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "Logout",
                            tint = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(24.dp)
                                .clickable { showLogoutDialog = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== PROFILE SECTION =====
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 16.dp)
                    ) {
                        // Profile picture and info - left aligned
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar image
                            Image(
                                painter = painterResource(id = R.drawable.avatar),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            // 16dp spacing
                            Spacer(modifier = Modifier.width(16.dp))

                            // User information - Sử dụng dữ liệu từ ViewModel
                            Column {
                                // Name - Hiển thị từ API
                                Text(
                                    text = uiState.userName,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )

                                // 4dp spacing
                                Spacer(modifier = Modifier.height(4.dp))

                                // Email - Hiển thị từ API
                                Text(
                                    text = uiState.userEmail,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                )
                            }
                        }
                    }

                    // Tăng khoảng cách giữa profile section và menu items
                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== MENU ITEMS =====
                    // My Orders - Sử dụng số lượng đơn hàng thực tế
                    MenuItem(
                        title = "My orders",
                        subtitle = "Already have ${uiState.orderCount} orders",
                        onClick = { navController.navigate("myOrder") }
                    )

                    // Tăng khoảng cách giữa các menu items
                    Spacer(modifier = Modifier.height(16.dp))

                    // Shipping Addresses - Sử dụng số lượng địa chỉ thực tế
                    MenuItem(
                        title = "Shipping Addresses",
                        subtitle = "${String.format("%02d", uiState.addressCount)} Addresses",
                        onClick = { navController.navigate("shippingAddress") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment Method - Sử dụng số lượng phương thức thanh toán thực tế
                    MenuItem(
                        title = "Payment Method",
                        subtitle = "You have ${uiState.paymentMethodCount} cards",
                        onClick = { navController.navigate("paymentmethod") }
                    )

                    // Tăng khoảng cách giữa các menu items
                    Spacer(modifier = Modifier.height(16.dp))

                    // My reviews - Sử dụng số lượng đánh giá thực tế
                    MenuItem(
                        title = "My reviews",
                        subtitle = "Reviews for ${uiState.reviewCount} items",
                        onClick = { navController.navigate("myReviews") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Setting
                    MenuItem(
                        title = "Setting",
                        subtitle = "Notification, Password, FAQ, Contact",
                        onClick = { navController.navigate("setting") }
                    )

                    // Hiển thị lỗi nếu có
                    uiState.error?.let { errorMsg ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMsg,
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    // Reduced outer padding for proper menu item sizing
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left information
            Column(modifier = Modifier.weight(1f)) {
                // Title
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )

                // 8dp spacing between title and subtitle
                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }

            // Right arrow icon
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "More",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}