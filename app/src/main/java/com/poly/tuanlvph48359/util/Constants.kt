package com.poly.tuanlvph48359.util

object Constants {
    // API base URL - thay đổi tùy theo cài đặt của bạn
    // Cho Android Emulator, sử dụng 10.0.2.2 để truy cập localhost
    // Cho thiết bị thực, sử dụng địa chỉ IP của máy tính
    const val API_BASE_URL = "http://10.24.43.68:3000/"

    // ID người dùng cho test (trong ứng dụng thực, bạn sẽ lấy từ đăng nhập)
    const val TEST_USER_ID = "1"

    // Request Codes
    const val REQUEST_CODE_LOGIN = 100
    const val REQUEST_CODE_SIGNUP = 101

    // Shared Preferences
    const val PREFS_NAME = "furniture_app_prefs"
    const val KEY_USER_ID = "user_id"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
}