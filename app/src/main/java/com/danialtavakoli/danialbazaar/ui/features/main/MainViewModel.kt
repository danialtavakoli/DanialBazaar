package com.danialtavakoli.danialbazaar.ui.features.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danialtavakoli.danialbazaar.model.data.Ads
import com.danialtavakoli.danialbazaar.model.data.CheckOut
import com.danialtavakoli.danialbazaar.model.data.Product
import com.danialtavakoli.danialbazaar.model.repository.cart.CartRepository
import com.danialtavakoli.danialbazaar.model.repository.product.ProductRepository
import com.danialtavakoli.danialbazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val isInternetConnected: Boolean
) : ViewModel() {
    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgressBar = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)
    val paymentResultDialog = mutableStateOf(false)
    val checkoutData = mutableStateOf(CheckOut(null, null))

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (isInternetConnected) showProgressBar.value = true
            delay(1200)
            val dataProducts = async { productRepository.getAllProducts(isInternetConnected) }
            val dataAds = async { productRepository.getAllAds(isInternetConnected) }
            updateData(dataProducts.await(), dataAds.await())
            showProgressBar.value = false
        }
    }

    private fun updateData(products: List<Product>, ads: List<Ads>) {
        dataProducts.value = products
        dataAds.value = ads
    }

    fun loadBadgeNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }
    }

    fun getPaymentStatus(): Int {
        return cartRepository.getPurchaseStatus()
    }

    fun setPaymentStatus(status: Int) {
        cartRepository.setPurchaseStatus(status)
    }

    fun getCheckoutData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = cartRepository.checkOut(cartRepository.getOrderId())
            if (result.success!!) {
                checkoutData.value = result
                paymentResultDialog.value = true
            }
        }
    }
}