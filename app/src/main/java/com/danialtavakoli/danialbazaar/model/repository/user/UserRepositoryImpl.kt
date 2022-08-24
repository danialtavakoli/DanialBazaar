package com.danialtavakoli.danialbazaar.model.repository.user

import android.content.SharedPreferences
import com.danialtavakoli.danialbazaar.model.net.ApiService
import com.danialtavakoli.danialbazaar.model.repository.TokenInMemory
import com.danialtavakoli.danialbazaar.util.VALUE_SUCCESS
import com.google.gson.JsonObject

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) : UserRepository {
    override suspend fun signUp(name: String, userName: String, password: String): String {
        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", userName)
            addProperty("password", password)
        }
        val result = apiService.signUp(jsonObject)
        return if (result.success) {
            TokenInMemory.refreshToken(userName, result.token)
            saveToken(result.token)
            saveUserName(userName)
            saveUserLoginTime()
            VALUE_SUCCESS
        } else result.message
    }

    override suspend fun signIn(userName: String, password: String): String {
        val jsonObject = JsonObject().apply {
            addProperty("email", userName)
            addProperty("password", password)
        }
        val result = apiService.signIn(jsonObject)
        return if (result.success) {
            TokenInMemory.refreshToken(userName, result.token)
            saveToken(result.token)
            saveUserName(userName)
            saveUserLoginTime()
            VALUE_SUCCESS
        } else result.message
    }

    override fun signOut() {
        TokenInMemory.refreshToken(null, null)
        sharedPreferences.edit().clear().apply()
    }

    override fun loadToken() {
        TokenInMemory.refreshToken(getUserName(), getToken())
    }

    override fun saveToken(newToken: String) {
        sharedPreferences.edit().putString("token", newToken).apply()
    }

    override fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    override fun saveUserName(userName: String) {
        sharedPreferences.edit().putString("userName", userName).apply()
    }

    override fun getUserName(): String? {
        return sharedPreferences.getString("userName", null)
    }

    override fun saveUserLocation(address: String, postalCode: String) {
        sharedPreferences.edit().putString("address", address).apply()
        sharedPreferences.edit().putString("postalCode", postalCode).apply()
    }

    override fun getUserLocation(): Pair<String, String> {
        val address = sharedPreferences.getString("address", "Click to add")!!
        val postalCode = sharedPreferences.getString("postalCode", "Click to add")!!
        return Pair(address, postalCode)
    }

    override fun saveUserLoginTime() {
        val currentTime = System.currentTimeMillis()
        sharedPreferences.edit().putString("loginTime", currentTime.toString()).apply()
    }

    override fun getUserLoginTime(): String {
        return sharedPreferences.getString("loginTime", "0")!!
    }
}