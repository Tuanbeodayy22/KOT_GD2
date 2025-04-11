// ProductResponse.kt
package com.poly.tuanlvph48359.model

data class ProductResponse(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String,
    val rating: Double,
    val reviewCount: Int,
    val inStock: Boolean,
    val colors: List<String>
)