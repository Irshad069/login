package com.login.ui.screen.signup

data class SignupState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    var passwordVisible: Boolean = false,
    var confirmPasswordVisible: Boolean = false
)

sealed class SignupIntent {
    data class Signup(val email: String, val password: String, val confirmPassword: String) :
        SignupIntent()

    data class UpdateEmail(val email: String) : SignupIntent()
    data class UpdatePassword(val password: String) : SignupIntent()
    data class UpdateConfirmPassword(val confirmPassword: String) : SignupIntent()
    data object TogglePasswordVisibility : SignupIntent()
    data object ConfirmTogglePasswordVisibility : SignupIntent()
}

sealed class SignupEffect {
    data object NavigateBack : SignupEffect()
    data class ShowToast(val message: String) : SignupEffect()
}
