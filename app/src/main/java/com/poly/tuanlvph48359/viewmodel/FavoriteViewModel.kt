package com.poly.tuanlvph48359.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.FavoriteItem
import com.poly.tuanlvph48359.repository.FavoriteRepository
import com.poly.tuanlvph48359.util.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application, private val repository: FavoriteRepository) : AndroidViewModel(application) {
    private val TAG = "FAVORITE_DEBUG"
    private val userPreferences = UserPreferences(application)
    private val apiService = RetrofitClient.apiService
    // StateFlow cho danh sách yêu thích
    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favorites: StateFlow<List<FavoriteItem>> = _favorites.asStateFlow()

    // Trạng thái loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Lưu userId hiện tại để kiểm tra thay đổi
    private var currentUserId = ""

    // Gọi khi màn hình được hiển thị
    fun loadFavorites() {
        val userId = userPreferences.getUserId()

        // Kiểm tra nếu đã đổi user hoặc chưa có dữ liệu
        if (currentUserId != userId || _favorites.value.isEmpty()) {
            currentUserId = userId
            // Reset state và load lại dữ liệu
            _favorites.value = emptyList()
            fetchFavorites()
        } else {
            // Nếu cùng user, vẫn refresh để đảm bảo dữ liệu mới nhất
            fetchFavorites()
        }
    }

    // Lấy danh sách yêu thích từ API
    private fun fetchFavorites() {
        viewModelScope.launch {
            try {
                val userId = userPreferences.getUserId()
                if (userId.isEmpty()) {
                    _error.value = "Bạn cần đăng nhập để xem danh sách yêu thích"
                    return@launch
                }

                _isLoading.value = true
                _error.value = null

                val response = apiService.getFavorites(userId)
                if (response.isSuccessful) {
                    response.body()?.let { items ->
                        _favorites.value = items
                        Log.d(TAG, "Đã tải ${items.size} mục yêu thích")
                    } ?: run {
                        _favorites.value = emptyList()
                        Log.d(TAG, "Không có mục yêu thích nào")
                    }
                } else {
                    _error.value = "Lỗi khi tải: response = ${response.code()}, message = ${response.message()}"
                    Log.e(TAG, "Lỗi khi tải: response = ${response.code()}, message = ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Lỗi khi tải dữ liệu: ${e.message}"
                Log.e(TAG, "Lỗi khi tải dữ liệu", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Xóa mục yêu thích
    fun removeFromFavorites(productId: Int) {
        val userId = userPreferences.getUserId()

        // XÓA TRỰC TIẾP khỏi UI
        val itemToRemove = _favorites.value.find { it.id == productId }
        if (itemToRemove != null) {
            _favorites.value = _favorites.value.filter { it.id != itemToRemove.id }
            Log.d(TAG, "Đã xóa khỏi UI: ${itemToRemove.name}")
        }

        // Gọi API xóa khỏi server
        viewModelScope.launch {
            try {
                val deleteResponse = apiService.removeFromFavorites(userId, productId)

                if (deleteResponse.isSuccessful) {
                    Log.d(TAG, "Đã xóa thành công khỏi server: ID = $productId")
                    // KHÔNG cần gọi lại fetchFavorites()
                } else {
                    Log.e(TAG, "Xóa khỏi server thất bại: ${deleteResponse.code()} ${deleteResponse.message()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi xóa mục yêu thích trên server: ${e.message}")
            }
        }
    }





    // Thêm vào yêu thích
    fun addToFavorites(item: FavoriteItem) {
        viewModelScope.launch {
            try {
                val response = apiService.addToFavorites(item)
                if (response.isSuccessful) {
                    // Refresh danh sách sau khi thêm
                    fetchFavorites()
                    Log.d(TAG, "Đã thêm ${item.name} vào danh sách yêu thích")
                } else {
                    _error.value = "Không thể thêm vào yêu thích"
                    Log.e(TAG, "Lỗi khi thêm: ${response.code()}, ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = "Lỗi khi thêm vào yêu thích: ${e.message}"
                Log.e(TAG, "Lỗi khi thêm vào yêu thích", e)
            }
        }
    }

    // Reset state khi logout
    fun resetState() {
        _favorites.value = emptyList()
        _error.value = null
        currentUserId = ""
    }
}