package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.poly.tuanlvph48359.R

data class ReviewItem(
    val id: Int,
    val productName: String,
    val price: Double,
    val rating: Int,
    val date: String,
    val comment: String,
    val productImage: Int
)

@Composable
fun MyReviewsScreen(navController: NavController) {
    // Sample reviews data
    val reviews = remember {
        listOf(
            ReviewItem(
                id = 1,
                productName = "Coffee Table",
                price = 50.00,
                rating = 5,
                date = "20/03/2020",
                comment = "Nice Furniture with good delivery. The delivery time is very fast. Then products look like exactly the picture in the app. Besides, color is also the same and quality is very good despite very cheap price",
                productImage = R.drawable.ban
            ),
            ReviewItem(
                id = 2,
                productName = "Coffee Table",
                price = 50.00,
                rating = 5,
                date = "20/03/2020",
                comment = "Nice Furniture with good delivery. The delivery time is very fast. Then products look like exactly the picture in the app. Besides, color is also the same and quality is very good despite very cheap price",
                productImage = R.drawable.ban
            ),
            ReviewItem(
                id = 3,
                productName = "Coffee Table",
                price = 50.00,
                rating = 5,
                date = "20/03/2020",
                comment = "Nice Furniture with good delivery. The delivery time is very fast. Then products look like exactly the picture in the app. Besides, color is also the same and quality is very good despite very cheap price",
                productImage = R.drawable.ban
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Nền trắng như thiết kế
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // App Bar - Với padding top 60dp để tránh camera notch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
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

                // Title
                Text(
                    text = "My reviews",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                // Search icon
                IconButton(
                    onClick = { /* Open search */ },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Divider
            Divider(
                color = Color(0xFFEEEEEE),
                thickness = 1.dp
            )

            // Reviews list
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(reviews) { review ->
                    ReviewItemCard(review = review)

                    // Divider between items
                    Divider(
                        color = Color(0xFFEEEEEE),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewItemCard(review: ReviewItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA)) // Màu nền xám nhạt
            .padding(16.dp)
    ) {
        // Product info row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            Image(
                painter = painterResource(id = review.productImage),
                contentDescription = review.productName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Product name and price
            Column {
                Text(
                    text = review.productName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$ ${review.price}0",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rating and date row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Star rating
            Row {
                repeat(review.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700), // Gold color
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Date
            Text(
                text = review.date,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Review comment
        Text(
            text = review.comment,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        )
    }
}
