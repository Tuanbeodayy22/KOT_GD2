package com.poly.tuanlvph48359.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.poly.tuanlvph48359.util.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel chung cho toàn ứng dụng, quản lý trạng thái và sự kiện toàn cục
 */
class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    // StateFlow để theo dõi thay đổi người dùng
    private val _userChangeEvent = MutableStateFlow(0)
    val userChangeEvent: StateFlow<Int> = _userChangeEvent.asStateFlow()

    // Thêm sự kiện đăng xuất
    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    // Khởi tạo để lắng nghe sự kiện từ UserPreferences
    init {
        // Subscribe vào userChangeFlow từ UserPreferences
        observeUserChanges()
    }

    private fun observeUserChanges() {
        // Trong môi trường thực, bạn sẽ sử dụng coroutines/flow để collect
        // Ở đây chúng ta chỉ để ý đến sự thay đổi của userPreferences.userChangeFlow
        // và cập nhật _userChangeEvent khi có thay đổi
        _userChangeEvent.value = userPreferences.userChangeFlow.value
    }

    // Phương thức để đăng xuất và thông báo cho các màn hình khác
    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            userPreferences.logout()
            _userChangeEvent.value += 1
            _logoutEvent.emit(Unit) // Phát sự kiện đăng xuất
            onComplete()
        }
    }

    // Phương thức để đăng nhập và thông báo cho các màn hình khác
    fun login(userId: String, onComplete: () -> Unit) {
        userPreferences.saveUserLoginInfo(userId)
        _userChangeEvent.value += 1
        onComplete()
    }
}