package com.poly.tuanlvph48359.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.model.UserResponse
import com.poly.tuanlvph48359.repository.AuthRepository
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewModel quản lý trạng thái xác thực (đăng nhập, đăng ký)
 */
class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    // Trạng thái đăng nhập
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    // Trạng thái đăng ký
    private val _registerState = MutableStateFlow(LoginState())
    val registerState: StateFlow<LoginState> = _registerState

    // Đăng nhập người dùng
    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _loginState.value = LoginState(
                        user = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _loginState.value = LoginState(
                        isError = true,
                        errorMessage = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _loginState.value = LoginState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    // Đăng ký người dùng mới
    fun registerUser(name: String, email: String, password: String) {
        repository.registerUser(name, email, password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _registerState.value = LoginState(
                        user = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _registerState.value = LoginState(
                        isError = true,
                        errorMessage = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _registerState.value = LoginState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    // Reset trạng thái đăng nhập (sử dụng sau khi đăng nhập thành công)
    fun resetLoginState() {
        _loginState.value = LoginState()
    }

    // Reset trạng thái đăng ký (sử dụng sau khi đăng ký thành công)
    fun resetRegisterState() {
        _registerState.value = LoginState()
    }
}