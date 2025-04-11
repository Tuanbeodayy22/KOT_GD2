package com.poly.tuanlvph48359.util

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.poly.tuanlvph48359.util.Constants.KEY_IS_LOGGED_IN
import com.poly.tuanlvph48359.util.Constants.KEY_USER_ID
import com.poly.tuanlvph48359.util.Constants.PREFS_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Lớp tiện ích để quản lý thông tin đăng nhập của người dùng
 */
class UserPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // StateFlow để theo dõi thay đổi người dùng
    private val _userChangeFlow = MutableStateFlow(0)
    val userChangeFlow: StateFlow<Int> = _userChangeFlow

    // Lưu thông tin đăng nhập
    fun saveUserLoginInfo(userId: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
        // Thông báo thay đổi người dùng
        _userChangeFlow.value += 1
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Lấy ID người dùng hiện tại
    fun getUserId(): String {
        // Xử lý trường hợp userId có thể được lưu dưới dạng Integer
        return try {
            prefs.getString(KEY_USER_ID, "") ?: ""
        } catch (e: ClassCastException) {
            // Nếu userId được lưu dưới dạng Integer, chuyển đổi nó thành String
            prefs.getInt(KEY_USER_ID, 0).toString()
        }
    }

    // Đăng xuất - xóa thông tin đăng nhập
    fun logout() {
        prefs.edit().apply {
            clear()
            apply()
        }
        // Thông báo thay đổi người dùng
        _userChangeFlow.value += 1
    }
}