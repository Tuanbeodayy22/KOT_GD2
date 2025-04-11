package com.poly.tuanlvph48359.repository

import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.OrderResponse
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class OrderRepository {
    private val apiService = RetrofitClient.apiService

    fun getOrders(userId: String): Flow<Resource<List<OrderResponse>>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getOrders(userId)
            if (response.isSuccessful) {
                response.body()?.let { orders ->
                    emit(Resource.Success(orders))
                } ?: emit(Resource.Error("Phản hồi rỗng"))
            } else {
                emit(Resource.Error("Không thể lấy đơn hàng: ${response.code()}"))
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