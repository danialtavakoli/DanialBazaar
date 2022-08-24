package com.danialtavakoli.danialbazaar.model.repository.product

import com.danialtavakoli.danialbazaar.model.data.Ads
import com.danialtavakoli.danialbazaar.model.data.Product
import com.danialtavakoli.danialbazaar.model.db.ProductDao
import com.danialtavakoli.danialbazaar.model.net.ApiService

class ProductRepositoryImpl(
    private val apiService: ApiService,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun getAllProducts(isInternetConnected: Boolean): List<Product> {
        if (isInternetConnected) {
            val data = apiService.getAllProducts()
            if (data.success) {
                productDao.insertOrUpdate(data.products)
                return data.products
            }
        } else return productDao.getAll()
        return listOf()
    }

    override suspend fun getAllAds(isInternetConnected: Boolean): List<Ads> {
        if (isInternetConnected) {
            val data = apiService.getAllAds()
            if (data.success) return data.ads
        }
        return listOf()
    }

    override suspend fun getAllProductsByCategory(category: String): List<Product> {
        return productDao.getAllByCategory(category)
    }

    override suspend fun getProductsById(productId: String): Product {
        return productDao.getProductById(productId)
    }
}