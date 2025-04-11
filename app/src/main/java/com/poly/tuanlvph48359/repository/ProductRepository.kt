package com.poly.tuanlvph48359.repository

import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.model.ProductResponse
import com.poly.tuanlvph48359.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository cho việc xử lý dữ liệu sản phẩm
 */
class ProductRepository {
    private val apiService = RetrofitClient.apiService

    // Lấy tất cả sản phẩm
    fun getProducts(): Flow<Resource<List<ProductResponse>>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    emit(Resource.Success(products))
                } ?: emit(Resource.Error("Phản hồi rỗng"))
            } else {
                emit(Resource.Error("Không thể lấy sản phẩm: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Đã xảy ra lỗi không mong muốn: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."))
        } catch (e: Exception) {
            emit(Resource.Error("Đã xảy ra lỗi: ${e.localizedMessage}"))
        }
    }

    // Lấy sản phẩm theo ID
    fun getProductById(id: Int): Flow<Resource<ProductResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getProductById(id)
            if (response.isSuccessful) {
                response.body()?.let { product ->
                    emit(Resource.Success(product))
                } ?: emit(Resource.Error("Phản hồi rỗng"))
            } else {
                emit(Resource.Error("Không thể lấy sản phẩm: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Đã xảy ra lỗi không mong muốn: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng."))
        } catch (e: Exception) {
            emit(Resource.Error("Đã xảy ra lỗi: ${e.localizedMessage}"))
        }
    }

    // Lấy sản phẩm theo danh mục
    fun getProductsByCategory(category: String): Flow<Resource<List<ProductResponse>>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getProductsByCategory(category)
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    emit(Resource.Success(products))
                } ?: emit(Resource.Error("Phản hồi rỗng"))
            } else {
                emit(Resource.Error("Không thể lấy sản phẩm: ${response.code()}"))
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