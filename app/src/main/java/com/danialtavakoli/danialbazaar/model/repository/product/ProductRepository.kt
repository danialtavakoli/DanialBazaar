package com.danialtavakoli.danialbazaar.model.repository.product

import com.danialtavakoli.danialbazaar.model.data.Ads
import com.danialtavakoli.danialbazaar.model.data.Product

interface ProductRepository {
    suspend fun getAllProducts(isInternetConnected: Boolean): List<Product>
    suspend fun getAllAds(isInternetConnected: Boolean): List<Ads>
    suspend fun getAllProductsByCategory(category: String): List<Product>
    suspend fun getProductsById(productId: String): Product
}