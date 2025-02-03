package com.login.ui.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state: StateFlow<SignupState> = _state

    private val _effects = MutableSharedFlow<SignupEffect>()
    val effects = _effects.asSharedFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onIntent(intent: SignupIntent) {
        when (intent) {
            is SignupIntent.Signup -> {
                signup(intent.email, intent.password, intent.confirmPassword)
            }

            is SignupIntent.UpdateEmail -> {
                _state.value = _state.value.copy(email = intent.email)
            }

            is SignupIntent.UpdatePassword -> {
                _state.value = _state.value.copy(password = intent.password)
            }

            is SignupIntent.UpdateConfirmPassword -> {
                _state.value = _state.value.copy(confirmPassword = intent.confirmPassword)
            }

            is SignupIntent.TogglePasswordVisibility -> {
                _state.value = _state.value.copy(passwordVisible = !_state.value.passwordVisible)
            }

            is SignupIntent.ConfirmTogglePasswordVisibility -> {
                _state.value =
                    _state.value.copy(confirmPasswordVisible = !_state.value.confirmPasswordVisible)
            }
        }
    }

    private fun signup(email: String, password: String, confirmPassword: String) {
        if (email.isBlank()) {
            _state.value = _state.value.copy(emailError = "Email cannot be empty")
            return
        }
        if (password.isBlank()) {
            _state.value = _state.value.copy(passwordError = "Password cannot be empty")
            return
        }
        if (password.length < 6) {
            _state.value =
                _state.value.copy(passwordError = "Password must be at least 6 characters")
            return
        }
        if (confirmPassword.isBlank()) {
            _state.value = _state.value.copy(passwordError = "Confirm Password cannot be empty")
            return
        }
        if (confirmPassword != password) {
            _state.value = _state.value.copy(confirmPasswordError = "Passwords do not match")
            return
        }

        _state.value = _state.value.copy(isSubmitting = true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _state.value = _state.value.copy(isSubmitting = false)

                if (task.isSuccessful) {
                    viewModelScope.launch {
                        _effects.emit(SignupEffect.ShowToast("Signup successful"))
                        _effects.emit(SignupEffect.NavigateBack)
                    }
                } else {
                    viewModelScope.launch {
                        _effects.emit(
                            SignupEffect.ShowToast(
                                task.exception?.localizedMessage ?: "Signup failed"
                            )
                        )
                    }
                }
            }
    }
}
