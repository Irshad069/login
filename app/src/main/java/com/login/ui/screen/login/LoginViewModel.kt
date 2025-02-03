package com.login.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _effects = MutableSharedFlow<SignInEffect>()
    val effects = _effects.asSharedFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> {
                login(intent.email, intent.password)
            }

            is LoginIntent.UpdateEmail -> {
                _state.value = _state.value.copy(email = intent.email)
                validateCredentials(intent.email, _state.value.password)
            }

            is LoginIntent.UpdatePassword -> {
                _state.value = _state.value.copy(password = intent.password)
                validateCredentials(_state.value.email, intent.password)
            }

            is LoginIntent.TogglePasswordVisibility -> {
                _state.value = _state.value.copy(passwordVisible = !_state.value.passwordVisible)
            }
        }
    }

    private fun validateCredentials(email: String, password: String) {
        val isEmailValid =
            email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        _state.value = _state.value.copy(
            emailError = if (isEmailValid) null else "Invalid email format",
            passwordError = if (isPasswordValid) null else "Password must be at least 6 characters",
            isSignInEnabled = isEmailValid && isPasswordValid
        )
    }

    private fun login(email: String, password: String) {
        if (!_state.value.isSignInEnabled) return

        _state.value = _state.value.copy(isSubmitting = true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        _effects.emit(SignInEffect.ShowToast("Sign in successful"))
                        _effects.emit(SignInEffect.Navigate)
                    }
                } else {
                    viewModelScope.launch {
                        _effects.emit(
                            SignInEffect.ShowToast(
                                task.exception?.localizedMessage ?: "Sign in  failed"
                            )
                        )
                    }
                }
            }

    }
}
