package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.clickable

@Composable
fun SettingScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Thanh tiêu đề
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút quay lại
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

                // Tiêu đề
                Text(
                    text = "Setting",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Khoảng trống để cân đối bố cục
                Spacer(modifier = Modifier.size(48.dp))
            }

            // Phần Thông tin cá nhân
            SettingSection(
                title = "Personal Information",
                onEditClick = { /* Chỉnh sửa thông tin cá nhân */ }
            ) {
                // Trường Tên
                SettingField(
                    label = "Name",
                    value = "Bruno Pham"
                )

                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // Trường Email
                SettingField(
                    label = "Email",
                    value = "bruno203@gmail.com"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phần Mật khẩu
            SettingSection(
                title = "Password",
                onEditClick = { /* Chỉnh sửa mật khẩu */ }
            ) {
                // Trường mật khẩu (đã ẩn)
                SettingField(
                    label = "Name",
                    value = "**************"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phần Thông báo
            SettingSection(
                title = "Notifications",
                showEditIcon = false
            ) {
                // Thông báo khuyến mãi
                NotificationToggle(
                    label = "Sales",
                    initialValue = true
                )

                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // Thông báo sản phẩm mới
                NotificationToggle(
                    label = "New arrivals",
                    initialValue = false
                )

                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // Thông báo thay đổi trạng thái giao hàng
                NotificationToggle(
                    label = "Delivery status changes",
                    initialValue = false
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phần Trung tâm trợ giúp
            SettingSection(
                title = "Help Center",
                showEditIcon = false
            ) {
                // Mục FAQ
                SettingLinkItem(
                    label = "FAQ",
                    onClick = { /* Điều hướng đến FAQ */ }
                )
            }

            // Khoảng trống dưới cùng
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingSection(
    title: String,
    showEditIcon: Boolean = true,
    onEditClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Tiêu đề phần với biểu tượng chỉnh sửa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            )

            if (showEditIcon && onEditClick != null) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray
                    )
                }
            }
        }

        // Nội dung phần trong card màu xám nhạt
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFAFAFA) // Màu xám rất nhạt
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Nhãn trường
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Giá trị trường
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
fun NotificationToggle(
    label: String,
    initialValue: Boolean
) {
    var isEnabled by remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nhãn công tắc
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        // Công tắc bật/tắt
        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50), // Màu xanh lá khi bật
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun SettingLinkItem(
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nhãn mục
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        // Biểu tượng mũi tên
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }
}
