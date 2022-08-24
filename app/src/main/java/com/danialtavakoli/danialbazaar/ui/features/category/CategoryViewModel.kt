package com.danialtavakoli.danialbazaar.ui.features.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danialtavakoli.danialbazaar.model.data.Product
import com.danialtavakoli.danialbazaar.model.repository.product.ProductRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val productRepository: ProductRepository) : ViewModel() {
    val dataProducts = mutableStateOf<List<Product>>(listOf())

    fun loadData(category: String) {
        viewModelScope.launch {
            val data = productRepository.getAllProductsByCategory(category)
            dataProducts.value = data
        }
    }
}