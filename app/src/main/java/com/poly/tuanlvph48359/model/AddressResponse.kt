package com.poly.tuanlvph48359.model

data class AddressResponse(
    val id: String,
    val address: String,
    val isDefault: Boolean,
    val name: String,
    val userId: String
)