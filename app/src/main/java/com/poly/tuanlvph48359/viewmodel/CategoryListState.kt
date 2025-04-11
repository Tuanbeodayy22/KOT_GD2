package com.poly.tuanlvph48359.viewmodel

import com.poly.tuanlvph48359.model.CategoryResponse

// Lớp trạng thái cho danh sách danh mục
data class CategoryListState(
    val isLoading: Boolean = false,
    val categories: List<CategoryResponse> = emptyList(),
    val error: String = ""
)