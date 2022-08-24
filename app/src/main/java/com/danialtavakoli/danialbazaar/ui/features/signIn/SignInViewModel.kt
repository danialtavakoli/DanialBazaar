package com.danialtavakoli.danialbazaar.ui.features.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danialtavakoli.danialbazaar.model.repository.user.UserRepository
import com.danialtavakoli.danialbazaar.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    fun signInUser(LoggingEvent: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = userRepository.signIn(email.value!!, password.value!!)
            LoggingEvent(result)
        }
    }
}