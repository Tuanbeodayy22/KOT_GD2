package com.poly.tuanlvph48359.model

data class NotificationResponse(
    val id: Int,
    val userId: String,
    val title: String,
    val description: String,
    val date: String,
    val isRead: Boolean,
    val type: String,
    val imageUrl: String
)