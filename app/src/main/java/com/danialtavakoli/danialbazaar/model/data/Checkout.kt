package com.danialtavakoli.danialbazaar.model.data

data class CheckOut(val order: Order?, val success: Boolean?) {
    data class Order(
        val amount: String,
        val creationTime: String,
        val `data`: Data,
        val orderId: String,
        val paymentTime: String,
        val status: String
    ) {
        data class Data(
            val address: String,
            val postalCode: String
        )
    }
}