package com.poly.tuanlvph48359.repository

import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.CategoryResponse
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository cho việc xử lý dữ liệu danh mục
 */
class CategoryRepository {
    private val apiService = RetrofitClient.apiService

    // Lấy tất cả danh mục
    fun getCategories(): Flow<Resource<List<CategoryResponse>>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getCategories()
            if (response.isSuccessful) {
                response.body()?.let { categories ->
                    emit(Resource.Success(categories))
                } ?: emit(Resource.Error("Phản hồi rỗng"))
            } else {
                emit(Resource.Error("Không thể lấy danh mục: ${response.code()}"))
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