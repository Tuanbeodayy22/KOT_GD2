package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.poly.tuanlvph48359.model.OrderResponse
import com.poly.tuanlvph48359.util.UserPreferences
import com.poly.tuanlvph48359.viewmodel.OrderViewModel

@Composable
fun MyOrderScreen(
    navController: NavController,
    orderViewModel: OrderViewModel = viewModel()
) {
    val ordersState = orderViewModel.ordersState.value
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userId = userPreferences.getUserId()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Delivered", "Processing", "Canceled")

    // Load orders when screen is displayed
    LaunchedEffect(userId) {
        orderViewModel.getOrders(userId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // App Bar - Điều chỉnh padding top thành 60dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tăng kích thước IconButton để dễ nhấn
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "My order",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Empty space to balance layout
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color.Black,
                divider = { Divider(thickness = 0.dp) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        modifier = Modifier.height(48.dp)
                    )
                }
            }

            // Selected Tab Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp / 3).dp)
                        .height(2.dp)
                        .background(Color.Black)
                        .offset(x = ((LocalConfiguration.current.screenWidthDp / 3) * selectedTab).dp)
                )
            }

            // Order List
            when {
                ordersState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
                ordersState.error.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = ordersState.error,
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                            Button(
                                onClick = { orderViewModel.getOrders(userId) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Thử lại")
                            }
                        }
                    }
                }
                ordersState.orders.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Bạn chưa có đơn hàng nào",
                            fontSize = 16.sp
                        )
                    }
                }
                else -> {
                    // Lọc đơn hàng theo tab đã chọn
                    val filteredOrders = when (selectedTab) {
                        0 -> ordersState.orders.filter { it.status == "Delivered" }
                        1 -> ordersState.orders.filter { it.status == "Processing" }
                        2 -> ordersState.orders.filter { it.status == "Canceled" }
                        else -> ordersState.orders
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(filteredOrders) { order ->
                            OrderCard(
                                order = order,
                                onDetailClick = {
                                    navController.navigate("order_detail/${order.id}")
                                }
                            )
                        }
                    }
                }
            }

            // Bottom Navigation
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 0.dp,
                modifier = Modifier.height(56.dp)
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("home") },
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
                    onClick = { navController.navigate("favorites") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.bookmark),
                            contentDescription = "Favorites",
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
                    onClick = { navController.navigate("notification") },
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
                    onClick = { navController.navigate("profile") },
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
    }
}

@Composable
fun OrderCard(
    order: OrderResponse,
    onDetailClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Order Number and Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order No${order.orderNumber}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )

                Text(
                    text = order.date,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity and Total Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quantity: ${order.items.sumOf { it.quantity }}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )

                Text(
                    text = "Total Amount: $${order.totalAmount}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Detail Button and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onDetailClick,
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Detail",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    )
                }

                Text(
                    text = order.status,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = when (order.status) {
                            "Delivered" -> Color(0xFF4CAF50)
                            "Processing" -> Color(0xFFFFA000)
                            "Canceled" -> Color(0xFFE53935)
                            else -> Color.Gray
                        }
                    )
                )
            }
        }
    }
}