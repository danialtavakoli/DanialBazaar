package com.danialtavakoli.danialbazaar.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ProductResponse(
    val success: Boolean,
    val products: List<Product>
)

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey
    val productId: String,
    val category: String,
    val detailText: String,
    val imgUrl: String,
    val material: String,
    val name: String,
    val price: String,
    val soldItem: String,
    val tags: String,
    val quantity: String?
)