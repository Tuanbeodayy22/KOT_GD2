package com.poly.tuanlvph48359.model

data class AddressCreateRequest(
    val address: String,
    val isDefault: Boolean,
    val name: String,
    val userId: String
)