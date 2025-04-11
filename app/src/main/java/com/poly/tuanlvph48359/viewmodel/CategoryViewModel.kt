package com.poly.tuanlvph48359.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.model.CategoryResponse
import com.poly.tuanlvph48359.repository.CategoryRepository
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel xử lý dữ liệu danh mục và trạng thái UI
 */
class CategoryViewModel : ViewModel() {

    private val repository = CategoryRepository()

    // Trạng thái cho danh sách danh mục
    private val _categoriesState = mutableStateOf(CategoryListState())
    val categoriesState: State<CategoryListState> = _categoriesState

    // Khởi tạo - tải danh mục
    init {
        getCategories()
    }

    // Lấy tất cả danh mục
    fun getCategories() {
        repository.getCategories().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _categoriesState.value = CategoryListState(isLoading = true)
                }
                is Resource.Success -> {
                    _categoriesState.value = CategoryListState(categories = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _categoriesState.value = CategoryListState(error = result.message ?: "Đã xảy ra lỗi")
                }
            }
        }.launchIn(viewModelScope)
    }
}