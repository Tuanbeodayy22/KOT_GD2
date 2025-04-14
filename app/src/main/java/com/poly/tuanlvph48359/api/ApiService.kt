package com.poly.tuanlvph48359.api

import com.poly.tuanlvph48359.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface defining all API endpoints for the furniture app
 */
interface ApiService {
    // Products
    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponse>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductResponse>

    @GET("products")
    suspend fun getProductsByCategory(@Query("category") category: String): Response<List<ProductResponse>>

    // Categories
    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryResponse>>

    // Cart
    @GET("cart")
    suspend fun getCartItems(@Query("userId") userId: String): Response<List<CartItemResponse>>

    @POST("cart")
    suspend fun addToCart(@Body cartItem: CartItemResponse): Response<CartItemResponse>

    @PUT("cart/{id}")
    suspend fun updateCartItem(@Path("id") id: Int, @Body cartItem: CartItemResponse): Response<CartItemResponse>

    @DELETE("cart/{id}")
    suspend fun removeFromCart(@Path("id") id: Int): Response<Unit>

    @GET("favorites")
    suspend fun getFavorites(@Query("userId") userId: String): Response<List<FavoriteItem>>

    @POST("favorites")
    suspend fun addToFavorites(@Body favoriteItem: FavoriteItem): Response<FavoriteResponse>

    // Cập nhật phương thức xóa mục yêu thích
    @HTTP(method = "DELETE", path = "favorites", hasBody = false)
    suspend fun removeFromFavorites(
        @Query("userId") userId: String,
        @Query("id") productId: Int
    ): Response<Unit>



    // Users
    @GET("users")
    suspend fun getUsersByEmail(@Query("email") email: String): Response<List<UserResponse>>
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<UserResponse>

    @POST("users")
    suspend fun registerUser(@Body user: UserRequest): Response<UserResponse>

    @GET("users")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<List<UserResponse>>

    // Orders
    @GET("orders")
    suspend fun getOrders(@Query("userId") userId: String): Response<List<OrderResponse>>

    @POST("orders")
    suspend fun createOrder(@Body order: OrderResponse): Response<OrderResponse>

    // Reviews
    @GET("reviews")
    suspend fun getUserReviews(@Query("userId") userId: String): Response<List<ReviewResponse>>

    @GET("reviews")
    suspend fun getUserReviews(@Query("userId") userId: Int): Response<List<ReviewResponse>>

    @POST("reviews")
    suspend fun addReview(@Body review: ReviewResponse): Response<ReviewResponse>

    // Addresses
    @GET("addresses")
    suspend fun getAddresses(@Query("userId") userId: String): Response<List<AddressResponse>>

    @POST("addresses")
    suspend fun addAddress(@Body address: AddressResponse): Response<AddressResponse>

    @POST("addresses")
    suspend fun createAddress(@Body address: AddressCreateRequest): Response<AddressResponse>

    @PUT("addresses/{id}")
    suspend fun updateAddress(@Path("id") id: String, @Body address: AddressResponse): Response<AddressResponse>

    @DELETE("addresses/{id}")
    suspend fun deleteAddress(@Path("id") id: String): Response<Unit>


    // Payment Methods
    @GET("paymentMethods")
    suspend fun getPaymentMethods(@Query("userId") userId: String): Response<List<PaymentMethodResponse>>

    @POST("paymentMethods")
    suspend fun addPaymentMethod(@Body paymentMethod: PaymentMethodResponse): Response<PaymentMethodResponse>

    @PUT("paymentMethods/{id}")
    suspend fun updatePaymentMethod(@Path("id") id: Int, @Body paymentMethod: PaymentMethodResponse): Response<PaymentMethodResponse>

    @DELETE("paymentMethods/{id}")
    suspend fun deletePaymentMethod(@Path("id") id: Int): Response<Unit>

    // Notifications
    @GET("notifications")
    suspend fun getNotifications(@Query("userId") userId: String): Response<List<NotificationResponse>>

    @PUT("notifications/{id}")
    suspend fun markNotificationAsRead(@Path("id") id: Int, @Body notification: NotificationResponse): Response<NotificationResponse>
}