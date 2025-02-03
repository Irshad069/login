package com.login.ui.screen.login

data class LoginState(
    var email: String = "",
    val password: String = "",
    var emailError: String? = null,
    val passwordError: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    var passwordVisible: Boolean = false,
    val isSignInEnabled: Boolean = false
)

sealed class LoginIntent {
    data class Login(val email: String, val password: String) : LoginIntent()
    data class UpdateEmail(val email: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    data object TogglePasswordVisibility : LoginIntent()
}

sealed class SignInEffect {
    data object Navigate : SignInEffect()
    data class ShowToast(val message: String) : SignInEffect()
}
