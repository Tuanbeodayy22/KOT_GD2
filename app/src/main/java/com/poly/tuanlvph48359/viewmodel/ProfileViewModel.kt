package com.poly.tuanlvph48359.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.AddressResponse
import com.poly.tuanlvph48359.model.PaymentMethodResponse
import com.poly.tuanlvph48359.model.ReviewResponse
import com.poly.tuanlvph48359.model.UserResponse
import com.poly.tuanlvph48359.util.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)
    private val apiService = RetrofitClient.apiService

    // UI State để quản lý trạng thái màn hình
    var uiState by mutableStateOf(ProfileUiState())
        private set

    // Khởi tạo
    init {
        loadUserProfile()
        loadUserStats()
    }

    // Tải dữ liệu người dùng
    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val userId = userPreferences.getUserId()
                if (userId.isNotEmpty()) {
                    uiState = uiState.copy(isLoading = true)

                    // Sử dụng API getUserById để lấy thông tin người dùng
                    val response = apiService.getUserById(userId)

                    if (response.isSuccessful) {
                        response.body()?.let { user ->
                            uiState = uiState.copy(
                                userId = user.id,
                                userName = user.name,
                                userEmail = user.email,
                                isLoading = false,
                                error = null
                            )
                        } ?: run {
                            uiState = uiState.copy(
                                userId = userId,
                                isLoading = false,
                                error = "Không thể tải thông tin người dùng: Phản hồi rỗng"
                            )
                        }
                    } else {
                        uiState = uiState.copy(
                            userId = userId,
                            isLoading = false,
                            error = "Không thể tải thông tin người dùng: ${response.code()}"
                        )
                    }
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "Người dùng chưa đăng nhập"
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Lỗi: ${e.message}"
                )
            }
        }
    }

    // Tải thông tin bổ sung (số lượng đơn hàng, địa chỉ, phương thức thanh toán, đánh giá)
    private fun loadUserStats() {
        viewModelScope.launch {
            try {
                val userId = userPreferences.getUserId()
                if (userId.isNotEmpty()) {
                    // Tải số lượng đơn hàng
                    apiService.getOrders(userId).also { response ->
                        if (response.isSuccessful) {
                            val orderCount = response.body()?.size ?: 0
                            uiState = uiState.copy(orderCount = orderCount)
                        }
                    }

                    // Tải số lượng địa chỉ
                    apiService.getAddresses(userId).also { response ->
                        if (response.isSuccessful) {
                            val addressCount = response.body()?.size ?: 0
                            uiState = uiState.copy(addressCount = addressCount)
                        }
                    }

                    // Tải số lượng phương thức thanh toán
                    apiService.getPaymentMethods(userId).also { response ->
                        if (response.isSuccessful) {
                            val paymentMethodCount = response.body()?.size ?: 0
                            uiState = uiState.copy(paymentMethodCount = paymentMethodCount)
                        }
                    }

                    // Tải số lượng đánh giá
                    apiService.getUserReviews(userId).also { response ->
                        if (response.isSuccessful) {
                            val reviewCount = response.body()?.size ?: 0
                            uiState = uiState.copy(reviewCount = reviewCount)
                        }
                    }
                }
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần, nhưng không cập nhật UI state để tránh ghi đè lỗi quan trọng hơn
            }
        }
    }

    // Làm mới dữ liệu
    fun refreshData() {
        loadUserProfile()
        loadUserStats()
    }
}

// UI State cho màn hình profile
data class ProfileUiState(
    val isLoading: Boolean = true,
    val userId: String = "",
    val userName: String = "Người dùng",
    val userEmail: String = "email@example.com",
    val orderCount: Int = 0,
    val addressCount: Int = 0,
    val paymentMethodCount: Int = 0,
    val reviewCount: Int = 0,
    val error: String? = null
)