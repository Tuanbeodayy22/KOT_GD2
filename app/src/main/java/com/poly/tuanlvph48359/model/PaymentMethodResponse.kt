package com.poly.tuanlvph48359.model

data class PaymentMethodResponse(
    val id: Int,
    val userId: String,
    val type: String,
    val cardNumber: String,
    val isDefault: Boolean
)