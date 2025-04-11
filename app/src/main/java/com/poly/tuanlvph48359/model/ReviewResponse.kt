package com.poly.tuanlvph48359.model

data class ReviewResponse(
    val id: Int,
    val productId: String,
    val userId: Int,
    val rating: Int,
    val comment: String,
    val date: String
)