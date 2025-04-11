package com.poly.tuanlvph48359.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.AddressCreateRequest
import com.poly.tuanlvph48359.model.AddressResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {
    private val apiService = RetrofitClient.apiService

    private val _addresses = MutableStateFlow<List<AddressResponse>>(emptyList())
    val addresses: StateFlow<List<AddressResponse>> = _addresses

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAddresses(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getAddresses(userId)
                if (response.isSuccessful) {
                    _addresses.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Không thể tải địa chỉ: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun addAddress(address: AddressResponse): Boolean {
        return try {
            _isLoading.value = true

            // Tạo đối tượng AddressCreateRequest không có trường ID
            val addressRequest = AddressCreateRequest(
                address = address.address,
                isDefault = address.isDefault,
                name = address.name,
                userId = address.userId
            )

            // Gọi API với đối tượng không có ID
            val response = apiService.createAddress(addressRequest)

            if (response.isSuccessful) {
                // Thêm vào danh sách hiện tại (ID đã được server tạo)
                val currentList = _addresses.value.toMutableList()
                response.body()?.let { currentList.add(it) }
                _addresses.value = currentList
                true
            } else {
                _error.value = "Không thể thêm địa chỉ: ${response.code()}"
                false
            }
        } catch (e: Exception) {
            _error.value = "Lỗi: ${e.message}"
            false
        } finally {
            _isLoading.value = false
        }
    }

    fun updateAddress(id: String, address: AddressResponse) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.updateAddress(id, address)
                if (response.isSuccessful) {
                    // Cập nhật danh sách hiện tại
                    val currentList = _addresses.value.toMutableList()
                    val index = currentList.indexOfFirst { it.id == id }
                    if (index != -1) {
                        response.body()?.let { currentList[index] = it }
                        _addresses.value = currentList
                    }
                } else {
                    _error.value = "Không thể cập nhật địa chỉ: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteAddress(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.deleteAddress(id)
                if (response.isSuccessful) {
                    // Xóa khỏi danh sách hiện tại
                    val currentList = _addresses.value.toMutableList()
                    currentList.removeIf { it.id == id }
                    _addresses.value = currentList
                } else {
                    _error.value = "Không thể xóa địa chỉ: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setDefaultAddress(id: String, userId: String) {
        viewModelScope.launch {
            try {
                // Lấy địa chỉ hiện tại
                val currentList = _addresses.value.toMutableList()
                val addressToUpdate = currentList.find { it.id == id } ?: return@launch

                // Cập nhật tất cả địa chỉ khác thành không mặc định
                currentList.forEach { address ->
                    if (address.id != id && address.isDefault) {
                        // Cập nhật trên server
                        apiService.updateAddress(address.id, address.copy(isDefault = false))
                    }
                }

                // Cập nhật địa chỉ được chọn thành mặc định
                apiService.updateAddress(id, addressToUpdate.copy(isDefault = true))

                // Tải lại danh sách
                loadAddresses(userId)
            } catch (e: Exception) {
                _error.value = "Lỗi: ${e.message}"
            }
        }
    }
}