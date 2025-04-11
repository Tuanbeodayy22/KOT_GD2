
package com.poly.tuanlvph48359.repository

import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.UserResponse
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class UserRepository {
    private val apiService = RetrofitClient.apiService

    fun getUserById(userId: String): Flow<Resource<UserResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getUserById(userId)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    emit(Resource.Success(user))
                } ?: emit(Resource.Error("Phản hồi rỗng"))
            } else {
                emit(Resource.Error("Không thể lấy thông tin người dùng: ${response.code()}"))
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