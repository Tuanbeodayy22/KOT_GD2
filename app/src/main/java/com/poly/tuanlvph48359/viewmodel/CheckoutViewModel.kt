package com.poly.tuanlvph48359.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.AddressResponse
import com.poly.tuanlvph48359.model.CartItem
import com.poly.tuanlvph48359.model.OrderItemResponse
import com.poly.tuanlvph48359.model.OrderResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CheckoutViewModel : ViewModel() {

    var checkoutState by mutableStateOf(CheckoutState())
        private set

    private val apiService = RetrofitClient.apiService

    fun createOrder(
        userId: String,
        cartItems: List<CartItem>,
        totalAmount: Int,
        shippingAddress: AddressResponse?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                checkoutState = checkoutState.copy(isLoading = true)

                val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                val orderNumber = UUID.randomUUID().toString().substring(0, 8)

                // Chuyển đổi CartItems thành OrderItems
                val orderItems = cartItems.map { item ->
                    OrderItemResponse(
                        productId = item.id,
                        quantity = item.quantity,
                        price = item.price
                    )
                }

                // Tạo đối tượng đơn hàng
                val newOrder = OrderResponse(
                    id = 0, // API sẽ tự động tạo ID
                    userId = userId,
                    orderNumber = orderNumber,
                    date = currentDate,
                    status = "Processing",
                    totalAmount = totalAmount,
                    items = orderItems,
                    shippingAddress = shippingAddress // Thêm địa chỉ giao hàng
                )

                // Gửi API request
                val response = apiService.createOrder(newOrder)

                if (response.isSuccessful) {
                    checkoutState = checkoutState.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                    onSuccess()
                } else {
                    checkoutState = checkoutState.copy(
                        isLoading = false,
                        error = "Đặt hàng thất bại: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                checkoutState = checkoutState.copy(
                    isLoading = false,
                    error = "Đã xảy ra lỗi: ${e.message}"
                )
            }
        }
    }
}

data class CheckoutState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)