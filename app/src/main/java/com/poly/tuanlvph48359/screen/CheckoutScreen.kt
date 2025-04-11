package com.poly.tuanlvph48359.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import com.poly.tuanlvph48359.model.AddressResponse
import com.poly.tuanlvph48359.util.UserPreferences
import com.poly.tuanlvph48359.viewmodel.AddressViewModel
import com.poly.tuanlvph48359.viewmodel.CartViewModel
import com.poly.tuanlvph48359.viewmodel.CheckoutViewModel

@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    checkoutViewModel: CheckoutViewModel = viewModel(),
    addressViewModel: AddressViewModel = viewModel()
) {
    // Dữ liệu đơn hàng
    val orderTotal = cartViewModel.getTotal()
    val deliveryFee = 5.00
    val total = orderTotal + deliveryFee

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userId = userPreferences.getUserId()
    val checkoutState = checkoutViewModel.checkoutState

    // Lấy các địa chỉ từ ViewModel
    val addresses by addressViewModel.addresses.collectAsState()
    val defaultAddress = addresses.find { it.isDefault }

    // Tải địa chỉ khi màn hình hiển thị
    LaunchedEffect(userId) {
        addressViewModel.loadAddresses(userId)
    }

    // Hiển thị thông báo lỗi nếu có
    if (checkoutState.error != null) {
        LaunchedEffect(checkoutState.error) {
            Toast.makeText(context, checkoutState.error, Toast.LENGTH_LONG).show()
        }
    }

    // Xử lý khi đặt hàng thành công
    LaunchedEffect(checkoutState.isSuccess) {
        if (checkoutState.isSuccess) {
            navController.navigate("success") {
                popUpTo("home") { inclusive = false }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Thanh tiêu đề
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = "Check out",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(2f)
                )

                Spacer(Modifier.weight(1f))
            }

            // Nội dung checkout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                // Địa chỉ giao hàng
                SectionWithEditButton(
                    title = "Shipping Address",
                    onEditClick = { navController.navigate("shippingAddress") }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if (defaultAddress != null) {
                                // Hiển thị địa chỉ mặc định
                                Text(
                                    text = defaultAddress.name,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = defaultAddress.address,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                )
                            } else {
                                // Hiển thị thông báo khi không có địa chỉ mặc định
                                Text(
                                    text = "No default address selected",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Please add an address in your profile",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    ),
                                    modifier = Modifier.clickable {
                                        navController.navigate("shippingAddress")
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phương thức thanh toán
                SectionWithEditButton(
                    title = "Payment",
                    onEditClick = { /* Mở trang chỉnh sửa thanh toán */ }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mastercard),
                                contentDescription = "MasterCard",
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "**** **** **** 3947",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phương thức giao hàng
                SectionWithEditButton(
                    title = "Delivery method",
                    onEditClick = { /* Mở trang chỉnh sửa phương thức giao hàng */ }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.dhl),
                                contentDescription = "DHL",
                                modifier = Modifier.height(24.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "Fast (2-3days)",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị danh sách sản phẩm
                SectionWithEditButton(
                    title = "Items",
                    onEditClick = { navController.navigate("cart") }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if (cartViewModel.cartItems.isNotEmpty()) {
                                cartViewModel.cartItems.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Hình ảnh sản phẩm
                                        Image(
                                            painter = painterResource(id = item.imageRes),
                                            contentDescription = item.name,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(RoundedCornerShape(4.dp)),
                                            contentScale = ContentScale.Crop
                                        )

                                        Spacer(modifier = Modifier.width(16.dp))

                                        // Thông tin sản phẩm
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = item.name,
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.Black
                                                )
                                            )

                                            Text(
                                                text = "Quantity: ${item.quantity}",
                                                style = TextStyle(
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            )
                                        }

                                        // Giá
                                        Text(
                                            text = "$${String.format("%.2f", item.price * item.quantity)}",
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        )
                                    }

                                    // Đường phân cách giữa các mục (trừ mục cuối cùng)
                                    if (item != cartViewModel.cartItems.last()) {
                                        Divider(
                                            color = Color(0xFFEEEEEE),
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            } else {
                                // Nếu giỏ hàng trống
                                Text(
                                    text = "Your cart is empty",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Tổng kết đơn hàng
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Order:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        )
                        Text(
                            text = "$ ${String.format("%.2f", orderTotal)}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Delivery:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        )
                        Text(
                            text = "$ ${String.format("%.2f", deliveryFee)}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        )
                        Text(
                            text = "$ ${String.format("%.2f", total)}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                }
            }

            // Nút Submit Order
            Button(
                onClick = {
                    checkoutViewModel.createOrder(
                        userId = userId,
                        cartItems = cartViewModel.cartItems,
                        totalAmount = total.toInt(),
                        shippingAddress = defaultAddress,
                        onSuccess = { /* không cần xử lý gì ở đây */ }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp),
                enabled = !checkoutState.isLoading && defaultAddress != null // Chỉ cho phép đặt hàng khi có địa chỉ mặc định
            ) {
                if (checkoutState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "SUBMIT ORDER",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SectionWithEditButton(
    title: String,
    onEditClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            )

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.Gray
                )
            }
        }

        content()
    }
}