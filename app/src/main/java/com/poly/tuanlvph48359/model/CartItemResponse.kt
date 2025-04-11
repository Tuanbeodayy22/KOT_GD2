// CartItemResponse.kt
package com.poly.tuanlvph48359.model

data class CartItemResponse(
    val id: Int,
    val userId: String,
    val productId: Int,
    val quantity: Int
)