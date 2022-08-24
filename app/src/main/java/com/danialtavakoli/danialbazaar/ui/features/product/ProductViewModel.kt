package com.danialtavakoli.danialbazaar.ui.features.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danialtavakoli.danialbazaar.model.data.Comment
import com.danialtavakoli.danialbazaar.model.repository.cart.CartRepository
import com.danialtavakoli.danialbazaar.model.repository.comment.CommentRepository
import com.danialtavakoli.danialbazaar.model.repository.product.ProductRepository
import com.danialtavakoli.danialbazaar.util.EMPTY_PRODUCT
import com.danialtavakoli.danialbazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())
    val isAddingProduct = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)

    fun loadData(productID: String, isInternetConnected: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductsById(productID)
        }
        if (isInternetConnected) {
            loadComments(productID)
            loadBadgeNumber()
        }
    }

    fun addNewComment(productID: String, text: String, IsSuccess: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            commentRepository.addNewComment(productID, text, IsSuccess)
            delay(100)
            comments.value = commentRepository.getAllComments(productID)
        }
    }

    fun addProductToCart(productID: String, AddingToCartResult: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            isAddingProduct.value = true
            val result = cartRepository.addToCart(productID)
            delay(500)
            isAddingProduct.value = false
            if (result) AddingToCartResult.invoke("Product added to cart")
            else AddingToCartResult.invoke("Product not added")
        }
    }

    private fun loadComments(productID: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            comments.value = commentRepository.getAllComments(productID)
        }
    }

    private fun loadBadgeNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }
    }
}