package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.poly.tuanlvph48359.R

/**
 * Màn hình đánh giá và review sản phẩm
 * @param navController Controller để điều hướng
 * @param productId ID của sản phẩm đang xem review
 */
@Composable
fun ReviewScreen(navController: NavController, productId: Int = 2) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // App Bar - Đã điều chỉnh padding top để đồng bộ với các màn hình khác
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 16.dp), // Thay đổi từ vertical: 16dp thành top: 60dp
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button - Đã tăng kích thước để dễ nhấn
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp) // Thay đổi từ 24dp thành 48dp
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp) // Giữ kích thước icon là 24dp
                )
            }

            // Title
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Rating & Review",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black // Thêm màu rõ ràng
                )
            }

            // Empty space to balance the layout
            Spacer(modifier = Modifier.size(48.dp)) // Tăng kích thước để cân đối với IconButton
        }

        // Product info header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            Image(
                painter = painterResource(id = R.drawable.stand),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Product info
            Column {
                Text(
                    text = "Minimal Stand",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700), // Gold color for stars
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "4.5",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Text(
                    text = "10 reviews",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Reviews list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // First review - Bruno Fernandes
            item {
                ReviewItem(
                    avatarRes = R.drawable.avatar_bruno,
                    name = "Bruno Fernandes",
                    date = "20/03/2020",
                    rating = 5,
                    comment = "Nice Furniture with good delivery. The delivery time is very fast. Then products look like exactly the picture in the app. Besides, color is also the same and quality is very good despite very cheap price"
                )
            }

            // Second review - Tracy Mosby
            item {
                ReviewItem(
                    avatarRes = R.drawable.avatar_tracy,
                    name = "Tracy Mosby",
                    date = "20/03/2020",
                    rating = 5,
                    comment = "Nice Furniture with good delivery. The delivery time is very fast. Then products look like exactly the picture in the app. Besides, color is also the same and quality is very good despite very cheap price"
                )
            }

            // Third review (partially visible)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    // Only show the avatar at the top
                    Image(
                        painter = painterResource(id = R.drawable.avatar_tracyavatar),
                        contentDescription = "Reviewer Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        // Write review button
        Button(
            onClick = { /* Handle write review */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(
                text = "Write a review",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

/**
 * Item hiển thị một review của người dùng
 */
@Composable
fun ReviewItem(
    avatarRes: Int,
    name: String,
    date: String,
    rating: Int,
    comment: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // User info and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Name
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }

                // Date
                Text(
                    text = date,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Rating stars
            Row(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                repeat(rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFFFD700), // Gold color
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }

            // Review content
            Text(
                text = comment,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.DarkGray
            )
        }
    }
}
