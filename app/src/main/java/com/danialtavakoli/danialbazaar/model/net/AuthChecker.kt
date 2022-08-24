package com.danialtavakoli.danialbazaar.model.net

import com.danialtavakoli.danialbazaar.model.data.LoginResponse
import com.danialtavakoli.danialbazaar.model.repository.TokenInMemory
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthChecker : Authenticator, KoinComponent {
    private val apiService: ApiService by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (TokenInMemory.token != null
            && !response.request.url.pathSegments.last().equals("refreshToken", false)
            && refreshToken()
        ) return response.request
        return null
    }

    private fun refreshToken(): Boolean {
        val request: retrofit2.Response<LoginResponse> = apiService.refreshToken().execute()
        return request.body() != null && request.body()!!.success
    }
}