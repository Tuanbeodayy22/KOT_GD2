package com.poly.tuanlvph48359.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.model.ProductResponse
import com.poly.tuanlvph48359.repository.ProductRepository
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel xử lý dữ liệu sản phẩm và trạng thái UI
 */
class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    // Trạng thái cho danh sách sản phẩm
    private val _productsState = mutableStateOf(ProductListState())
    val productsState: State<ProductListState> = _productsState

    // Trạng thái cho chi tiết sản phẩm
    private val _productDetailState = mutableStateOf(ProductDetailState())
    val productDetailState: State<ProductDetailState> = _productDetailState

    // Tìm kiếm sản phẩm
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    // Khởi tạo - tải sản phẩm
    init {
        getAllProducts()
    }

    // Lấy tất cả sản phẩm
    fun getAllProducts() {
        repository.getProducts().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _productsState.value = ProductListState(isLoading = true)
                }
                is Resource.Success -> {
                    _productsState.value = ProductListState(products = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _productsState.value = ProductListState(error = result.message ?: "Đã xảy ra lỗi")
                }
            }
        }.launchIn(viewModelScope)
    }

    // Lấy sản phẩm theo ID
    fun getProductById(id: Int) {
        repository.getProductById(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _productDetailState.value = ProductDetailState(isLoading = true)
                }
                is Resource.Success -> {
                    _productDetailState.value = ProductDetailState(product = result.data)
                }
                is Resource.Error -> {
                    _productDetailState.value = ProductDetailState(error = result.message ?: "Đã xảy ra lỗi")
                }
            }
        }.launchIn(viewModelScope)
    }

    // Lấy sản phẩm theo danh mục
    fun getProductsByCategory(category: String) {
        repository.getProductsByCategory(category).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _productsState.value = ProductListState(isLoading = true)
                }
                is Resource.Success -> {
                    _productsState.value = ProductListState(products = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _productsState.value = ProductListState(error = result.message ?: "Đã xảy ra lỗi")
                }
            }
        }.launchIn(viewModelScope)
    }

    // Cập nhật query tìm kiếm và thực hiện tìm kiếm
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchProducts()
    }

    // Tìm kiếm sản phẩm dựa trên query
    private fun searchProducts() {
        val query = _searchQuery.value.lowercase().trim()
        if (query.isEmpty()) {
            // Nếu query rỗng, hiển thị tất cả sản phẩm
            getAllProducts()
        } else {
            // Tìm kiếm dựa trên danh sách sản phẩm hiện có
            val filteredProducts = _productsState.value.products.filter {
                it.name.lowercase().contains(query)
            }
            _productsState.value = ProductListState(products = filteredProducts)
        }
    }
}

// Lớp trạng thái cho danh sách sản phẩm
data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<ProductResponse> = emptyList(),
    val error: String = ""
)

// Lớp trạng thái cho chi tiết sản phẩm
data class ProductDetailState(
    val isLoading: Boolean = false,
    val product: ProductResponse? = null,
    val error: String = ""
)