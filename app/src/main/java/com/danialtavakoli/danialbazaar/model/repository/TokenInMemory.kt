package com.danialtavakoli.danialbazaar.model.repository

object TokenInMemory {
    var userName: String? = null
        private set
    var token: String? = null
        private set

    fun refreshToken(userName: String?, newToken: String?) {
        this.userName = userName
        this.token = newToken
    }
}