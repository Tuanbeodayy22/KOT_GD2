package com.poly.tuanlvph48359.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.CartItem
import com.poly.tuanlvph48359.model.CartItemResponse
import com.poly.tuanlvph48359.model.Product
import com.poly.tuanlvph48359.util.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitClient.apiService
    private val userPreferences = UserPreferences(application)

    // Danh sách sản phẩm trong giỏ hàng
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    // Trạng thái loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Thông báo lỗi
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Khởi tạo - tải giỏ hàng từ server khi ViewModel được tạo
    init {
        loadCartFromServer()
    }

    // Tải giỏ hàng từ server
    fun loadCartFromServer() {
        val userId = userPreferences.getUserId()
        if (userId.isEmpty()) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getCartItems(userId)
                if (response.isSuccessful) {
                    response.body()?.let { items ->
                        // Chuyển đổi CartItemResponse thành CartItem
                        val cartItems = items.mapNotNull { item ->
                            // Lấy thông tin sản phẩm từ API
                            val productResponse = apiService.getProductById(item.productId)
                            if (productResponse.isSuccessful && productResponse.body() != null) {
                                val product = productResponse.body()!!
                                val imageResId = getApplication<Application>().resources
                                    .getIdentifier(product.image, "drawable", getApplication<Application>().packageName)

                                CartItem(
                                    id = item.productId,
                                    name = product.name,
                                    price = product.price,
                                    quantity = item.quantity,
                                    imageRes = imageResId
                                )
                            } else {
                                null
                            }
                        }

                        _cartItems.clear()
                        _cartItems.addAll(cartItems)
                    }
                } else {
                    _error.value = "Không thể tải giỏ hàng: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Thêm sản phẩm vào giỏ hàng
    fun addToCart(product: Product) {
        val userId = userPreferences.getUserId()
        if (userId.isEmpty()) {
            // Nếu chưa đăng nhập, chỉ lưu trong bộ nhớ
            addToLocalCart(product)
            return
        }

        viewModelScope.launch {
            try {
                // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
                val existingItem = _cartItems.find { it.id == product.id }

                if (existingItem != null) {
                    // Cập nhật số lượng trên server
                    val cartItemResponse = CartItemResponse(
                        id = existingItem.id,
                        userId = userId,
                        productId = existingItem.id,
                        quantity = existingItem.quantity + 1
                    )

                    val response = apiService.updateCartItem(existingItem.id, cartItemResponse)
                    if (response.isSuccessful) {
                        // Cập nhật UI
                        val index = _cartItems.indexOf(existingItem)
                        _cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
                    } else {
                        _error.value = "Không thể cập nhật giỏ hàng"
                    }
                } else {
                    // Thêm mới vào server
                    val cartItemResponse = CartItemResponse(
                        id = 0, // Server sẽ gán id
                        userId = userId,
                        productId = product.id,
                        quantity = 1
                    )

                    val response = apiService.addToCart(cartItemResponse)
                    if (response.isSuccessful) {
                        // Thêm vào UI
                        _cartItems.add(
                            CartItem(
                                id = product.id,
                                name = product.name,
                                price = product.price,
                                quantity = 1,
                                imageRes = product.imageRes
                            )
                        )
                    } else {
                        _error.value = "Không thể thêm vào giỏ hàng"
                        // Thêm vào bộ nhớ trong trường hợp lỗi
                        addToLocalCart(product)
                    }
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
                // Thêm vào bộ nhớ trong trường hợp lỗi
                addToLocalCart(product)
            }
        }
    }

    // Thêm vào giỏ hàng local (trong bộ nhớ)
    private fun addToLocalCart(product: Product) {
        val existingItem = _cartItems.find { it.id == product.id }

        if (existingItem != null) {
            // Nếu đã có, tăng số lượng
            val index = _cartItems.indexOf(existingItem)
            _cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Nếu chưa có, thêm mới
            _cartItems.add(
                CartItem(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = 1,
                    imageRes = product.imageRes
                )
            )
        }
    }

    // Tăng số lượng
    // Tăng số lượng
    fun increaseQuantity(id: Int) {
        val userId = userPreferences.getUserId()
        val index = _cartItems.indexOfFirst { it.id == id }

        if (index != -1) {
            val item = _cartItems[index]
            val newQuantity = item.quantity + 1
            val originalItem = item.copy() // Lưu lại item gốc để khôi phục nếu cần

            // Cập nhật UI trước để giao diện phản hồi ngay lập tức
            _cartItems[index] = item.copy(quantity = newQuantity)

            if (userId.isNotEmpty()) {
                // Cập nhật trên server
                viewModelScope.launch {
                    try {
                        // Tìm cartItemId thực tế dựa trên userId và productId
                        val cartResponse = apiService.getCartItems(userId)
                        if (cartResponse.isSuccessful) {
                            val cartItems = cartResponse.body()
                            val cartItem = cartItems?.find { it.productId == id && it.userId == userId }

                            if (cartItem != null) {
                                // Sử dụng ID của cart item, không phải product ID
                                val cartItemResponse = CartItemResponse(
                                    id = cartItem.id,       // ID của mục giỏ hàng
                                    userId = userId,
                                    productId = id,         // ID của sản phẩm
                                    quantity = newQuantity
                                )

                                // Đảm bảo gọi API với cartItem.id, không phải product id
                                val response = apiService.updateCartItem(cartItem.id, cartItemResponse)
                                if (!response.isSuccessful) {
                                    // Nếu cập nhật server thất bại, đặt lại UI
                                    _cartItems[index] = originalItem
                                    _error.value = "Không thể cập nhật số lượng: ${response.code()}"
                                }
                            } else {
                                // Không tìm thấy mục giỏ hàng trên server
                                _error.value = "Không tìm thấy sản phẩm trong giỏ hàng"
                                // Khôi phục UI
                                _cartItems[index] = originalItem
                            }
                        } else {
                            // Không thể lấy danh sách giỏ hàng
                            _error.value = "Không thể tải giỏ hàng: ${cartResponse.code()}"
                            // Khôi phục UI
                            _cartItems[index] = originalItem
                        }
                    } catch (e: Exception) {
                        _error.value = "Lỗi: ${e.message}"
                        // Khôi phục UI
                        _cartItems[index] = originalItem
                    }
                }
            }
        }
    }

    // Giảm số lượng
    fun decreaseQuantity(id: Int) {
        val userId = userPreferences.getUserId()
        val index = _cartItems.indexOfFirst { it.id == id }

        if (index != -1) {
            val item = _cartItems[index]
            if (item.quantity > 1) {
                val newQuantity = item.quantity - 1
                val originalItem = item.copy() // Lưu lại item gốc để khôi phục nếu cần

                // Cập nhật UI trước để giao diện phản hồi ngay lập tức
                _cartItems[index] = item.copy(quantity = newQuantity)

                if (userId.isNotEmpty()) {
                    // Cập nhật trên server
                    viewModelScope.launch {
                        try {
                            // Tìm cartItemId thực tế dựa trên userId và productId
                            val cartResponse = apiService.getCartItems(userId)
                            if (cartResponse.isSuccessful) {
                                val cartItems = cartResponse.body()
                                val cartItem = cartItems?.find { it.productId == id && it.userId == userId }

                                if (cartItem != null) {
                                    // Sử dụng ID của cart item, không phải product ID
                                    val cartItemResponse = CartItemResponse(
                                        id = cartItem.id,       // ID của mục giỏ hàng
                                        userId = userId,
                                        productId = id,         // ID của sản phẩm
                                        quantity = newQuantity
                                    )

                                    // Đảm bảo gọi API với cartItem.id, không phải product id
                                    val response = apiService.updateCartItem(cartItem.id, cartItemResponse)
                                    if (!response.isSuccessful) {
                                        // Nếu cập nhật server thất bại, đặt lại UI
                                        _cartItems[index] = originalItem
                                        _error.value = "Không thể cập nhật số lượng: ${response.code()}"
                                    }
                                } else {
                                    // Không tìm thấy mục giỏ hàng trên server
                                    _error.value = "Không tìm thấy sản phẩm trong giỏ hàng"
                                    // Khôi phục UI
                                    _cartItems[index] = originalItem
                                }
                            } else {
                                // Không thể lấy danh sách giỏ hàng
                                _error.value = "Không thể tải giỏ hàng: ${cartResponse.code()}"
                                // Khôi phục UI
                                _cartItems[index] = originalItem
                            }
                        } catch (e: Exception) {
                            _error.value = "Lỗi: ${e.message}"
                            // Khôi phục UI
                            _cartItems[index] = originalItem
                        }
                    }
                }
            }
        }
    }
    // Xóa sản phẩm khỏi giỏ hàng
    fun removeFromCart(productId: Int) {
        val userId = userPreferences.getUserId()

        // Xóa từ UI ngay lập tức
        val itemToRemove = _cartItems.find { it.id == productId }
        if (itemToRemove != null) {
            _cartItems.remove(itemToRemove)
        }

        // Xóa từ server
        viewModelScope.launch {
            try {
                // Bước 1: Truy vấn để tìm mục cần xóa
                val response = apiService.getCartItems(userId)
                if (response.isSuccessful) {
                    val cartItems = response.body()
                    val itemToDelete = cartItems?.find { it.productId == productId && it.userId == userId }

                    // Bước 2: Nếu tìm thấy, xóa theo ID
                    if (itemToDelete != null && itemToDelete.id != 0) {
                        apiService.removeFromCart(itemToDelete.id)
                    }
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "Lỗi khi xóa mục giỏ hàng: ${e.message}")
            }
        }
    }

    // Tính tổng tiền
    fun getTotal(): Double {
        return _cartItems.sumOf { it.price * it.quantity }
    }

    // Xóa giỏ hàng khi đăng xuất
    fun clearCart() {
        _cartItems.clear()
    }

    // Xóa giỏ hàng sau khi đặt hàng thành công
    fun clearCartAfterCheckout() {
        val userId = userPreferences.getUserId()

        if (userId.isNotEmpty()) {
            // Xóa từng mục trên server
            viewModelScope.launch {
                try {
                    // Clone danh sách để tránh lỗi ConcurrentModificationException
                    val itemsToRemove = _cartItems.toList()

                    for (item in itemsToRemove) {
                        apiService.removeFromCart(item.id)
                    }

                    // Xóa tất cả từ UI
                    _cartItems.clear()
                } catch (e: Exception) {
                    _error.value = "Không thể xóa giỏ hàng sau khi đặt hàng: ${e.message}"
                    // Vẫn xóa khỏi UI trong trường hợp lỗi
                    _cartItems.clear()
                }
            }
        } else {
            // Xóa trực tiếp từ bộ nhớ
            _cartItems.clear()
        }
    }
}