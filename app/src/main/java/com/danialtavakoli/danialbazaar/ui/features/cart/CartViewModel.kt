package com.danialtavakoli.danialbazaar.ui.features.cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danialtavakoli.danialbazaar.model.data.Product
import com.danialtavakoli.danialbazaar.model.repository.cart.CartRepository
import com.danialtavakoli.danialbazaar.model.repository.user.UserRepository
import com.danialtavakoli.danialbazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val productList = mutableStateOf(listOf<Product>())
    val totalPrice = mutableStateOf(0)
    val isChangingNumber = mutableStateOf(Pair("", false))

    fun loadCartData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val data = cartRepository.getUserCartInfo()
            productList.value = data.productList
            totalPrice.value = data.totalPrice
        }
    }

    fun addItem(productID: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            isChangingNumber.value = isChangingNumber.value.copy(productID, true)
            val isSuccess = cartRepository.addToCart(productID)
            if (isSuccess) loadCartData()
            delay(100)
            isChangingNumber.value = isChangingNumber.value.copy(productID, false)
        }
    }

    fun removeItem(productID: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            isChangingNumber.value = isChangingNumber.value.copy(productID, true)
            val isSuccess = cartRepository.removeFromCart(productID)
            if (isSuccess) loadCartData()
            delay(100)
            isChangingNumber.value = isChangingNumber.value.copy(productID, false)
        }
    }

    fun getUserLocation(): Pair<String, String> {
        return userRepository.getUserLocation()
    }

    fun setUserLocation(address: String, postalCode: String) {
        userRepository.saveUserLocation(address, postalCode)
    }

    fun purchaseAll(address: String, postalCode: String, IsSuccess: (Boolean, String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = cartRepository.submitOrder(address, postalCode)
            IsSuccess.invoke(result.success, result.paymentLink)
        }
    }

    fun setPurchaseStatus(status: Int) {
        cartRepository.setPurchaseStatus(status)
    }
}