package com.poly.tuanlvph48359.repository

import com.poly.tuanlvph48359.api.ApiService
import com.poly.tuanlvph48359.model.FavoriteItem
import com.poly.tuanlvph48359.model.FavoriteResponse
import retrofit2.Response

class FavoriteRepository(private val apiService: ApiService) {

    // Lấy danh sách sản phẩm yêu thích theo userId
    suspend fun getFavorites(userId: String): Response<List<FavoriteItem>> {
        return apiService.getFavorites(userId)
    }

    // Thêm sản phẩm vào danh sách yêu thích
    suspend fun addToFavorites(item: FavoriteItem): Response<FavoriteResponse> {
        return apiService.addToFavorites(item)
    }

    // Xóa sản phẩm khỏi danh sách yêu thích
    suspend fun removeFromFavorites(userId: String, productId: Int): Response<Unit> {
        return apiService.removeFromFavorites(userId, productId)
    }

}
