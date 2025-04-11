package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

data class PaymentCard(
    val id: Int,
    val cardType: CardType,
    val isDefault: Boolean = false
)

enum class CardType {
    MASTERCARD, VISA
}

@Composable
fun PaymentMethodScreen(navController: NavController) {
    // Sample payment cards
    val paymentCards = remember {
        mutableStateListOf(
            PaymentCard(
                id = 1,
                cardType = CardType.MASTERCARD,
                isDefault = true
            ),
            PaymentCard(
                id = 2,
                cardType = CardType.VISA,
                isDefault = false
            )
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                        text = "Payment method",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        ),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    // Empty space to balance layout
                    Spacer(modifier = Modifier.size(48.dp))
                }

                // List of payment cards
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(paymentCards) { card ->
                        PaymentCardItem(
                            card = card,
                            onSetDefault = { cardId ->
                                // Update default card
                                paymentCards.forEachIndexed { i, item ->
                                    paymentCards[i] = item.copy(isDefault = item.id == cardId)
                                }
                            }
                        )
                    }

                    // Spacing at the bottom for FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Add new card button (Floating Action Button)
        FloatingActionButton(
            onClick = { /* Navigate to add card screen */ },
            containerColor = Color.White,
            contentColor = Color.Black,
            elevation = FloatingActionButtonDefaults.elevation(4.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Card",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun PaymentCardItem(
    card: PaymentCard,
    onSetDefault: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Card container với kích thước cố định
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Chiều cao cố định
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            // Card Image - Sử dụng hình ảnh thay vì tạo card từ đầu
            val cardImageRes = when (card.cardType) {
                CardType.MASTERCARD -> R.drawable.ic_mastercard // Thay bằng hình ảnh thẻ MasterCard thực tế
                CardType.VISA -> R.drawable.visa // Thay bằng hình ảnh thẻ Visa thực tế
            }

            // Hiển thị hình ảnh thẻ với tỷ lệ phù hợp
            Image(
                painter = painterResource(id = cardImageRes),
                contentDescription = "Payment Card",
                contentScale = ContentScale.Fit, // Thay đổi từ FillWidth thành Fit để giữ tỷ lệ
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp) // Thêm padding để tránh bị sát cạnh
            )
        }

        // Use as default checkbox
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = card.isDefault,
                onCheckedChange = { onSetDefault(card.id) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Gray
                )
            )

            Text(
                text = "Use as default payment method",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}
