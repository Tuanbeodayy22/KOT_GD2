package com.poly.tuanlvph48359.model

data class OrderResponse(
    val id: Int,
    val userId: String,
    val orderNumber: String,
    val date: String,
    val status: String,
    val totalAmount: Int,
    val items: List<OrderItemResponse>,
    val shippingAddress: AddressResponse? = null
)