package com.danialtavakoli.danialbazaar.model.repository.cart

import android.content.SharedPreferences
import com.danialtavakoli.danialbazaar.model.data.CheckOut
import com.danialtavakoli.danialbazaar.model.data.SubmitOrder
import com.danialtavakoli.danialbazaar.model.data.UserCartInfo
import com.danialtavakoli.danialbazaar.model.net.ApiService
import com.danialtavakoli.danialbazaar.util.NO_PAYMENT
import com.google.gson.JsonObject

class CartRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : CartRepository {
    override suspend fun addToCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply { addProperty("productId", productId) }
        val result = apiService.addProductToCart(jsonObject)
        return result.success
    }

    override suspend fun getCartSize(): Int {
        val result = apiService.getUserCart()
        if (result.success) {
            var counter = 0
            result.productList.forEach { counter += (it.quantity ?: "0").toInt() }
            return counter
        }
        return 0
    }

    override suspend fun removeFromCart(productId: String): Boolean {
        val jsonObject = JsonObject().apply { addProperty("productId", productId) }
        val result = apiService.removeFromCart(jsonObject)
        return result.success
    }

    override suspend fun getUserCartInfo(): UserCartInfo {
        return apiService.getUserCart()
    }

    override suspend fun submitOrder(address: String, postalCode: String): SubmitOrder {
        val jsonObject = JsonObject().apply {
            addProperty("address", address)
            addProperty("postalCode", postalCode)
        }
        val result = apiService.submitOrder(jsonObject)
        setOrderId(result.orderId.toString())
        return result
    }

    override suspend fun checkOut(orderId: String): CheckOut {
        val jsonObject = JsonObject().apply { addProperty("orderId", orderId) }
        return apiService.checkOut(jsonObject)
    }

    override fun setOrderId(orderId: String) {
        sharedPreferences.edit().putString("orderId", orderId).apply()
    }

    override fun getOrderId(): String {
        return sharedPreferences.getString("orderId", "0")!!
    }

    override fun setPurchaseStatus(status: Int) {
        sharedPreferences.edit().putInt("purchaseStatus", status).apply()
    }

    override fun getPurchaseStatus(): Int {
        return sharedPreferences.getInt("purchaseStatus", NO_PAYMENT)
    }
}