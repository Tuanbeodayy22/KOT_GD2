package com.poly.tuanlvph48359.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.model.OrderResponse
import com.poly.tuanlvph48359.repository.OrderRepository
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OrderViewModel : ViewModel() {
    private val repository = OrderRepository()

    private val _ordersState = mutableStateOf(OrderListState())
    val ordersState: State<OrderListState> = _ordersState

    fun getOrders(userId: String) {
        repository.getOrders(userId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _ordersState.value = OrderListState(
                        orders = result.data ?: emptyList()
                    )
                }
                is Resource.Error -> {
                    _ordersState.value = OrderListState(
                        error = result.message ?: "Đã xảy ra lỗi"
                    )
                }
                is Resource.Loading -> {
                    _ordersState.value = OrderListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}

data class OrderListState(
    val isLoading: Boolean = false,
    val orders: List<OrderResponse> = emptyList(),
    val error: String = ""
)