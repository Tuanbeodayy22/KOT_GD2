package com.poly.tuanlvph48359.repository

import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.UserRequest
import com.poly.tuanlvph48359.model.UserResponse
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository xử lý các thao tác xác thực (đăng nhập, đăng ký)
 */
class AuthRepository {
    private val apiService = RetrofitClient.apiService

    // Đăng nhập người dùng
    fun loginUser(email: String, password: String): Flow<Resource<UserResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.loginUser(email, password)
            if (response.isSuccessful) {
                val users = response.body()
                if (users.isNullOrEmpty()) {
                    emit(Resource.Error("Email hoặc mật khẩu không đúng"))
                } else {
                    // Lấy người dùng đầu tiên khớp với email và password
                    emit(Resource.Success(users[0]))
                }
            } else {
                emit(Resource.Error("Đăng nhập thất bại: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Đã xảy ra lỗi không mong muốn: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."))
        } catch (e: Exception) {
            emit(Resource.Error("Đã xảy ra lỗi: ${e.localizedMessage}"))
        }
    }

    // Đăng ký người dùng
    fun registerUser(name: String, email: String, password: String): Flow<Resource<UserResponse>> = flow {
        try {
            emit(Resource.Loading())

            // Tạo đối tượng người dùng mới
            val newUser = UserRequest(
                name = name,
                email = email,
                password = password
            )

            // Gọi API đăng ký
            val response = apiService.registerUser(newUser)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    emit(Resource.Success(user))
                } ?: emit(Resource.Error("Phản hồi rỗng"))
            } else {
                emit(Resource.Error("Đăng ký thất bại: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Đã xảy ra lỗi không mong muốn: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."))
        } catch (e: Exception) {
            emit(Resource.Error("Đã xảy ra lỗi: ${e.localizedMessage}"))
        }
    }
}