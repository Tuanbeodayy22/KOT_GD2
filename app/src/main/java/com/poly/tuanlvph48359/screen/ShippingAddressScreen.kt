package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.poly.tuanlvph48359.model.AddressResponse
import com.poly.tuanlvph48359.util.UserPreferences
import com.poly.tuanlvph48359.viewmodel.AddressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddressScreen(
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel()
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userId = userPreferences.getUserId()

    val addresses by addressViewModel.addresses.collectAsState()
    val isLoading by addressViewModel.isLoading.collectAsState()
    val error by addressViewModel.error.collectAsState()

    // State để theo dõi địa chỉ mặc định được chọn trong UI
    var selectedAddressId by remember { mutableStateOf<String?>(null) }

    // Cập nhật selectedAddressId khi danh sách địa chỉ được tải
    LaunchedEffect(addresses) {
        if (addresses.isNotEmpty()) {
            // Tìm địa chỉ mặc định trong danh sách nếu có
            val defaultAddress = addresses.find { it.isDefault }
            selectedAddressId = defaultAddress?.id
        }
    }

    // Tải địa chỉ khi màn hình hiển thị
    LaunchedEffect(userId) {
        addressViewModel.loadAddresses(userId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    text = "Shipping address",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            // Nội dung - hiển thị danh sách địa chỉ hoặc trạng thái loading/error
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }

                    error != null -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = error ?: "Đã xảy ra lỗi",
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { addressViewModel.loadAddresses(userId) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Thử lại")
                            }
                        }
                    }

                    addresses.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Bạn chưa có địa chỉ nào",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate("addShippingAddress") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Thêm địa chỉ")
                            }
                        }
                    }

                    else -> {
                        // Address List
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(addresses) { address ->
                                AddressCard(
                                    address = address,
                                    isSelected = address.id == selectedAddressId,
                                    onSelectAddress = {
                                        // Cập nhật UI trước
                                        selectedAddressId = address.id

                                        // Sau đó cập nhật server
                                        addressViewModel.setDefaultAddress(address.id, userId)
                                    },
                                    onEditAddress = {
                                        // Điều hướng đến màn hình chỉnh sửa địa chỉ
                                        // (có thể triển khai sau)
                                    }
                                )
                            }

                            // Nút thêm địa chỉ
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    FloatingActionButton(
                                        onClick = { navController.navigate("addShippingAddress") },
                                        containerColor = Color.White,
                                        contentColor = Color.Black,
                                        elevation = FloatingActionButtonDefaults.elevation(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Address",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
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
fun AddressCard(
    address: AddressResponse,
    isSelected: Boolean,
    onSelectAddress: () -> Unit,
    onEditAddress: (String) -> Unit
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
            // Checkbox and Edit button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        if (!isSelected) {
                            onSelectAddress()
                        }
                    }
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { checked ->
                            if (checked && !isSelected) {
                                onSelectAddress()
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Black,
                            uncheckedColor = Color.Gray
                        )
                    )

                    Text(
                        text = "Use as the shipping address",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.clickable {
                            if (!isSelected) {
                                onSelectAddress()
                            }
                        }
                    )
                }

                IconButton(
                    onClick = { onEditAddress(address.id) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Address",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Address details
            Column(
                modifier = Modifier.padding(start = 36.dp, top = 8.dp)
            ) {
                Text(
                    text = address.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = address.address,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}