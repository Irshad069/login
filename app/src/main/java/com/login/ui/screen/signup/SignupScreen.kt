package com.login.ui.screen.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.login.R
import com.login.ui.screen.components.CustomButton
import com.login.ui.screen.components.CustomOutlinedTextField
import kotlinx.coroutines.flow.collectLatest
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun SignupScreen(navController: NavController, viewModel: SignupViewModel = viewModel()) {

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect->
            when (effect) {
                is SignupEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                is SignupEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }

        }
    }

    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id = R.drawable.screen_bg),
            contentDescription = "Signup Screen Background",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.sdp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(top = 80.sdp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Account",
                    color = colorResource(id = R.color.primary_color),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.ssp
                )
                Spacer(modifier = Modifier.padding(top = 21.sdp))
                Text(
                    text = "Create an account so you can explore all the existing jobs",
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.ssp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(bottom = 80.sdp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomOutlinedTextField(
                    value = state.email,
                    onValueChange = { email ->
                        viewModel.onIntent(SignupIntent.UpdateEmail(email))
                    },
                    label = "Email",
                    isError = state.emailError != null,
                    errorText = state.emailError ?: "",
                    visualTransformation = VisualTransformation.None
                )
                CustomOutlinedTextField(
                    value = state.password,
                    onValueChange = { password ->
                        viewModel.onIntent(SignupIntent.UpdatePassword(password))
                    },
                    label = "Password",
                    isError = state.passwordError != null,
                    errorText = state.passwordError ?: "",
                    visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.padding(top = 5.sdp),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.onIntent(SignupIntent.TogglePasswordVisibility) }) {
                            Icon(
                                painter = painterResource(id = if (state.passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = if (state.passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                CustomOutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = { confirmPassword ->
                        viewModel.onIntent(
                            SignupIntent.UpdateConfirmPassword(confirmPassword)
                        )
                    },
                    label = "Confirm Password",
                    isError = state.confirmPasswordError != null,
                    errorText = state.confirmPasswordError ?: "",
                    visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.padding(top = 5.sdp),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.onIntent(SignupIntent.ConfirmTogglePasswordVisibility)
                        }) {
                            Icon(
                                painter = painterResource(id = if (state.confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = if (state.confirmPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                CustomButton(
                    modifier = Modifier.padding(top = 58.sdp),
                    text = "Sign up",
                    onClick = {
                        viewModel.onIntent(
                            SignupIntent.Signup(
                                state.email,
                                state.password,
                                state.confirmPassword
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.padding(top = 29.sdp))
                Text(
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    },
                    text = "Already have an account",
                    color = colorResource(id = R.color.black),
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
}