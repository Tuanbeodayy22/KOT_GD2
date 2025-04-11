package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.poly.tuanlvph48359.R


@Composable
fun NotificationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
    ) {
        // Thanh tiêu đề ứng dụng - đã điều chỉnh padding top để đồng bộ với các màn hình khác
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon tìm kiếm
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )

            // Tiêu đề "Notification" ở giữa
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notification",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Danh sách thông báo dạng cuộn
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFEEEEEE))
        ) {
            // Thông báo đơn hàng đã xác nhận với nhãn "New"
            item {
                NotificationItem(
                    imageRes = R.drawable.noti1,
                    title = "Your order #123456789 has been confirmed",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Turpis pretium et in arcu adipiscing nec. Turpis pretium et in arcu adipiscing nec.",
                    label = "New",
                    labelColor = Color(0xFF4CAF50)
                )
            }

            // Thông báo đơn hàng đã hủy
            item {
                NotificationItem(
                    imageRes = R.drawable.noti2,
                    title = "Your order #123456789 has been canceled",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Turpis pretium et in arcu adipiscing nec. Turpis pretium et in arcu adipiscing nec.",
                    label = null,
                    labelColor = Color.Transparent
                )
            }

            // Thông báo khuyến mãi với nhãn "HOT!"
            item {
                PromotionNotificationItem(
                    title = "Discover hot sale furnitures this week.",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Turpis pretium et in arcu adipiscing nec. Turpis pretium et in arcu adipiscing nec. Turpis pretium et in arcu adipiscing nec.",
                    label = "HOT!",
                    labelColor = Color(0xFFE53935)
                )
            }

            // Thông báo đơn hàng đã giao thành công
            item {
                NotificationItem(
                    imageRes = R.drawable.ban,
                    title = "Your order #123456789 has been shipped successfully",
                    description = "Please help us to confirm and rate your order to get 10% discount code for next order.",
                    label = null,
                    labelColor = Color.Transparent
                )
            }

            // Thông báo đơn hàng đã xác nhận
            item {
                NotificationItem(
                    imageRes = R.drawable.catset,
                    title = "Your order #123456789 has been confirmed",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Turpis pretium et in arcu adipiscing nec. Turpis pretium et in arcu adipiscing nec.",
                    label = null,
                    labelColor = Color.Transparent
                )
            }

            // Thông báo đơn hàng đã hủy
            item {
                NotificationItem(
                    imageRes = R.drawable.ban,
                    title = "Your order #123456789 has been canceled",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Turpis pretium et in arcu adipiscing nec. Turpis pretium et in arcu adipiscing nec.",
                    label = null,
                    labelColor = Color.Transparent
                )
            }

            // Thông báo đơn hàng đã giao thành công
            item {
                NotificationItem(
                    imageRes = R.drawable.ban,
                    title = "Your order #123456789 has been shipped successfully",
                    description = "Please help us to confirm and rate your order to get 10% discount code for next order.",
                    label = null,
                    labelColor = Color.Transparent
                )
            }
        }

        // Thanh điều hướng dưới cùng - giữ nguyên như yêu cầu
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Black,
            tonalElevation = 0.dp,
            modifier = Modifier.height(56.dp)
        ) {
            // Tab Trang chủ
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

            // Tab Yêu thích
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

            // Tab Thông báo (đang được chọn)
            NavigationBarItem(
                selected = true, // Đánh dấu là tab đang được chọn
                onClick = { /* Đã ở màn hình thông báo */ },
                icon = {
                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.notifications),
                            contentDescription = "Notifications",
                            modifier = Modifier.size(24.dp)
                        )
                        // Chỉ báo có thông báo mới (chấm đỏ)
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Red, CircleShape)
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )

            // Tab Tài khoản
            NavigationBarItem(
                selected = false,
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

/**
 * Thành phần hiển thị một mục thông báo với hình ảnh sản phẩm
 * @param imageRes ID resource của hình ảnh
 * @param title Tiêu đề thông báo
 * @param description Nội dung thông báo
 * @param label Nhãn (New, null)
 * @param labelColor Màu sắc của nhãn
 */
@Composable
fun NotificationItem(
    imageRes: Int,
    title: String,
    description: String,
    label: String?,
    labelColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hình ảnh sản phẩm
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Nội dung thông báo
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Tiêu đề thông báo
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Nhãn (nếu có)
                    if (label != null) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = labelColor,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Mô tả thông báo
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

/**
 * Thành phần hiển thị thông báo khuyến mãi không có hình ảnh
 * @param title Tiêu đề khuyến mãi
 * @param description Nội dung khuyến mãi
 * @param label Nhãn (HOT!)
 * @param labelColor Màu sắc của nhãn
 */
@Composable
fun PromotionNotificationItem(
    title: String,
    description: String,
    label: String,
    labelColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Tiêu đề khuyến mãi
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                // Nhãn HOT!
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = labelColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Mô tả khuyến mãi
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}
