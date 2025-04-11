package com.poly.tuanlvph48359.viewmodel

import com.poly.tuanlvph48359.model.UserResponse

/**
 * Data class chứa trạng thái đăng nhập
 */
data class LoginState(
    val isLoading: Boolean = false,
    val user: UserResponse? = null,
    val isError: Boolean = false,
    val errorMessage: String? = null
)